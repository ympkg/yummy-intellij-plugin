package com.ympkg.idea;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.externalSystem.model.DataNode;
import com.intellij.openapi.externalSystem.model.ExternalSystemException;
import com.intellij.openapi.externalSystem.model.ProjectKeys;
import com.intellij.openapi.externalSystem.model.project.*;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener;
import com.intellij.openapi.externalSystem.service.project.ExternalSystemProjectResolver;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class YummyProjectResolver implements ExternalSystemProjectResolver<YummyExecutionSettings> {

    @Override
    public DataNode<ProjectData> resolveProjectInfo(
            ExternalSystemTaskId id,
            String projectPath,
            boolean isPreviewMode,
            YummyExecutionSettings settings,
            ExternalSystemTaskNotificationListener listener) throws ExternalSystemException, IllegalArgumentException, IllegalStateException {

        String json = runYmIdeaJson(projectPath, settings);
        YummyProjectModel model = YummyProjectModel.parse(json);

        // Root project node
        ProjectData projectData = new ProjectData(
                YummyConstants.SYSTEM_ID,
                model.name,
                projectPath,
                projectPath);
        DataNode<ProjectData> projectNode = new DataNode<>(ProjectKeys.PROJECT, projectData, null);

        for (YummyProjectModel.Module module : model.modules) {
            addModuleNode(projectNode, module, projectPath);
        }

        return projectNode;
    }

    private void addModuleNode(DataNode<ProjectData> projectNode, YummyProjectModel.Module module, String projectPath) {
        String modulePath = module.path;

        ModuleData moduleData = new ModuleData(
                module.name,
                YummyConstants.SYSTEM_ID,
                "JAVA_MODULE",
                module.name,
                modulePath,
                modulePath);
        DataNode<ModuleData> moduleNode = projectNode.createChild(ProjectKeys.MODULE, moduleData);

        // Content root (source folders)
        ContentRootData contentRoot = new ContentRootData(YummyConstants.SYSTEM_ID, modulePath);
        for (YummyProjectModel.SourceFolder sf : module.getSourceFolders()) {
            String fullPath = modulePath + File.separator + sf.path.replace('/', File.separatorChar);
            switch (sf.type) {
                case "SOURCE":
                    contentRoot.storePath(ExternalSystemSourceType.SOURCE, fullPath);
                    break;
                case "TEST":
                    contentRoot.storePath(ExternalSystemSourceType.TEST, fullPath);
                    break;
                case "RESOURCE":
                    contentRoot.storePath(ExternalSystemSourceType.RESOURCE, fullPath);
                    break;
                case "TEST_RESOURCE":
                    contentRoot.storePath(ExternalSystemSourceType.TEST_RESOURCE, fullPath);
                    break;
            }
        }
        contentRoot.storePath(ExternalSystemSourceType.EXCLUDED, modulePath + File.separator + "out");
        moduleNode.createChild(ProjectKeys.CONTENT_ROOT, contentRoot);

        // Dependencies
        for (YummyProjectModel.Dependency dep : module.getDependencies()) {
            if ("library".equals(dep.type)) {
                addLibraryDependency(projectNode, moduleNode, dep);
            } else if ("module".equals(dep.type)) {
                ModuleDependencyData moduleDep = new ModuleDependencyData(moduleData, new ModuleData(
                        dep.name,
                        YummyConstants.SYSTEM_ID,
                        "JAVA_MODULE",
                        dep.name,
                        projectPath + File.separator + dep.name,
                        projectPath + File.separator + dep.name));
                moduleDep.setScope(mapScope(dep.scope));
                moduleNode.createChild(ProjectKeys.MODULE_DEPENDENCY, moduleDep);
            }
        }
    }

    private void addLibraryDependency(DataNode<ProjectData> projectNode, DataNode<ModuleData> moduleNode, YummyProjectModel.Dependency dep) {
        LibraryData libraryData = new LibraryData(YummyConstants.SYSTEM_ID, dep.name);
        if (dep.jarPath != null) {
            libraryData.addPath(LibraryPathType.BINARY, dep.jarPath);
        }
        if (dep.sourcePath != null) {
            libraryData.addPath(LibraryPathType.SOURCE, dep.sourcePath);
        }
        projectNode.createChild(ProjectKeys.LIBRARY, libraryData);

        LibraryDependencyData libDep = new LibraryDependencyData(
                moduleNode.getData(), libraryData, LibraryLevel.PROJECT);
        libDep.setScope(mapScope(dep.scope));
        moduleNode.createChild(ProjectKeys.LIBRARY_DEPENDENCY, libDep);
    }

    private static com.intellij.openapi.roots.DependencyScope mapScope(String scope) {
        if (scope == null) return com.intellij.openapi.roots.DependencyScope.COMPILE;
        switch (scope) {
            case "RUNTIME":  return com.intellij.openapi.roots.DependencyScope.RUNTIME;
            case "PROVIDED": return com.intellij.openapi.roots.DependencyScope.PROVIDED;
            case "TEST":     return com.intellij.openapi.roots.DependencyScope.TEST;
            default:         return com.intellij.openapi.roots.DependencyScope.COMPILE;
        }
    }

    private String runYmIdeaJson(String projectPath, YummyExecutionSettings settings) throws ExternalSystemException {
        try {
            GeneralCommandLine cmd = new GeneralCommandLine(settings.getYmExecutable(), "idea", "--json")
                    .withWorkDirectory(projectPath)
                    .withCharset(StandardCharsets.UTF_8);

            CapturingProcessHandler handler = new CapturingProcessHandler(cmd);
            ProcessOutput output = handler.runProcess(60_000);

            if (output.getExitCode() != 0) {
                throw new ExternalSystemException("ym idea --json failed (exit " + output.getExitCode() + "): " + output.getStderr());
            }
            return output.getStdout();
        } catch (ExternalSystemException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalSystemException("Failed to run ym idea --json: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean cancelTask(ExternalSystemTaskId taskId, ExternalSystemTaskNotificationListener listener) {
        return false;
    }
}
