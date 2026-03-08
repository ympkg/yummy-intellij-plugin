package com.ympkg.idea;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;

import java.util.List;

/**
 * Watches for package.toml changes and triggers ymc idea to refresh
 * IDEA project files (dependencies, source roots, etc).
 */
@Service(Service.Level.PROJECT)
public final class YummyFileWatcher implements BulkFileListener {

    private static final Logger LOG = Logger.getInstance(YummyFileWatcher.class);
    private final Project project;

    public YummyFileWatcher(Project project) {
        this.project = project;
    }

    @Override
    public void after(List<? extends VFileEvent> events) {
        boolean changed = events.stream()
                .anyMatch(e -> e.getPath().endsWith("package.toml"));

        if (changed) {
            LOG.info("package.toml changed, refreshing project...");
            YummyService.getInstance(project).runYmcIdea();
        }
    }
}
