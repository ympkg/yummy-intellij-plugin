package com.ympkg.idea;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.externalSystem.model.ExternalSystemException;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener;
import com.intellij.openapi.externalSystem.task.ExternalSystemTaskManager;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class YummyTaskManager implements ExternalSystemTaskManager<YummyExecutionSettings> {

    @Override
    public void executeTasks(
            ExternalSystemTaskId id,
            List<String> taskNames,
            String projectPath,
            YummyExecutionSettings settings,
            List<String> vmOptions,
            List<String> scriptParameters,
            String debuggerSetup,
            ExternalSystemTaskNotificationListener listener) throws ExternalSystemException {

        for (String task : taskNames) {
            executeTask(task, projectPath, settings, listener);
        }
    }

    private void executeTask(
            String task,
            String projectPath,
            YummyExecutionSettings settings,
            ExternalSystemTaskNotificationListener listener) throws ExternalSystemException {

        try {
            GeneralCommandLine cmd = new GeneralCommandLine(settings.getYmExecutable(), task)
                    .withWorkDirectory(projectPath)
                    .withCharset(StandardCharsets.UTF_8);

            CapturingProcessHandler handler = new CapturingProcessHandler(cmd);
            ProcessOutput output = handler.runProcess(300_000); // 5 min timeout

            if (output.getExitCode() != 0) {
                throw new ExternalSystemException("ym " + task + " failed: " + output.getStderr());
            }
        } catch (ExternalSystemException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalSystemException("Failed to run ym " + task + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean cancelTask(ExternalSystemTaskId id, ExternalSystemTaskNotificationListener listener) throws ExternalSystemException {
        return false;
    }
}
