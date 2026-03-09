package com.ympkg.idea;

import com.intellij.execution.configurations.SimpleJavaParameters;
import com.intellij.openapi.externalSystem.ExternalSystemAutoImportAware;
import com.intellij.openapi.externalSystem.ExternalSystemManager;
import com.intellij.openapi.externalSystem.model.ProjectSystemId;
import com.intellij.openapi.externalSystem.service.project.ExternalSystemProjectResolver;
import com.intellij.openapi.externalSystem.task.ExternalSystemTaskManager;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.util.Function;

import java.io.File;
import java.util.List;

public class YummyExternalSystemManager
        implements ExternalSystemManager<
            YummyProjectSettings,
            YummySettingsListener,
            YummySystemSettings,
            YummyLocalSettings,
            YummyExecutionSettings>,
        ExternalSystemAutoImportAware {

    @Override
    public ProjectSystemId getSystemId() {
        return YummyConstants.SYSTEM_ID;
    }

    @Override
    public Function<Project, YummySystemSettings> getSettingsProvider() {
        return YummySystemSettings::getInstance;
    }

    @Override
    public Function<Project, YummyLocalSettings> getLocalSettingsProvider() {
        return project -> project.getService(YummyLocalSettings.class);
    }

    @Override
    public Function<Pair<Project, String>, YummyExecutionSettings> getExecutionSettingsProvider() {
        return pair -> new YummyExecutionSettings();
    }

    @Override
    public Class<? extends ExternalSystemProjectResolver<YummyExecutionSettings>> getProjectResolverClass() {
        return YummyProjectResolver.class;
    }

    @Override
    public Class<? extends ExternalSystemTaskManager<YummyExecutionSettings>> getTaskManagerClass() {
        return YummyTaskManager.class;
    }

    @Override
    public FileChooserDescriptor getExternalProjectDescriptor() {
        return new FileChooserDescriptor(true, true, false, false, false, false)
                .withTitle("Select package.toml")
                .withDescription("Select the package.toml file or directory containing it");
    }

    @Override
    public void enhanceRemoteProcessing(SimpleJavaParameters parameters) throws com.intellij.execution.ExecutionException {
    }

    // ExternalSystemAutoImportAware — detect package.toml changes

    @Override
    public String getAffectedExternalProjectPath(String changedFileOrDirPath, Project project) {
        if (changedFileOrDirPath.endsWith(YummyConstants.PACKAGE_TOML)) {
            File file = new File(changedFileOrDirPath);
            return file.getParent();
        }
        return null;
    }
}
