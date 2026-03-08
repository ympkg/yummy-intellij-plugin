package com.ympkg.idea;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.DirectoryProjectConfigurator;

/**
 * Detects package.toml in the project root and triggers ymc idea
 * to generate IDEA project files on first open.
 */
public class YummyProjectConfigurator implements DirectoryProjectConfigurator {

    @Override
    public void configureProject(Project project, VirtualFile baseDir, Ref<Module> moduleRef, boolean isProjectCreatedWithWizard) {
        VirtualFile packageToml = baseDir.findChild("package.toml");
        if (packageToml == null) return;

        // Check if .iml files already exist (ymc idea already run)
        for (VirtualFile child : baseDir.getChildren()) {
            if ("iml".equals(child.getExtension())) return;
        }

        // Run ymc idea in background
        YummyService.getInstance(project).runYmcIdea();
    }
}
