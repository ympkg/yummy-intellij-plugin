package com.ympkg.idea;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFileManager;

import java.nio.charset.StandardCharsets;

/**
 * Central service for running ym/ymc commands within the IDE.
 */
@Service(Service.Level.PROJECT)
public final class YummyService {

    private static final Logger LOG = Logger.getInstance(YummyService.class);
    private final Project project;

    public YummyService(Project project) {
        this.project = project;
    }

    public static YummyService getInstance(Project project) {
        return project.getService(YummyService.class);
    }

    public void runYmcIdea() {
        runCommand("ymc", new String[]{"idea"}, "Yummy: Generating IDEA project files...");
    }

    public void runYmcBuild() {
        runCommand("ymc", new String[]{"build"}, "Yummy: Building...");
    }

    public void runYmcDev() {
        runCommand("ymc", new String[]{"dev"}, "Yummy: Starting dev mode...");
    }

    public void runYmcTest() {
        runCommand("ymc", new String[]{"test"}, "Yummy: Running tests...");
    }

    public void runYmAdd(String dep) {
        runCommand("ym", new String[]{"add", dep}, "Yummy: Adding " + dep + "...");
    }

    private void runCommand(String executable, String[] args, String title) {
        String basePath = project.getBasePath();
        if (basePath == null) return;

        try {
            GeneralCommandLine commandLine = new GeneralCommandLine(executable)
                    .withParameters(args)
                    .withWorkDirectory(basePath)
                    .withCharset(StandardCharsets.UTF_8);

            OSProcessHandler handler = new OSProcessHandler(commandLine);
            handler.addProcessListener(new ProcessAdapter() {
                @Override
                public void processTerminated(ProcessEvent event) {
                    if (event.getExitCode() == 0) {
                        LOG.info(title + " completed successfully");
                        VirtualFileManager.getInstance().asyncRefresh(null);
                    } else {
                        LOG.warn(title + " failed with exit code " + event.getExitCode());
                    }
                }

                @Override
                public void onTextAvailable(ProcessEvent event, Key outputType) {
                    LOG.info(event.getText().stripTrailing());
                }
            });
            handler.startNotify();
        } catch (Exception e) {
            LOG.error("Failed to run " + executable, e);
        }
    }
}
