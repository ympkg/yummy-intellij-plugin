package com.ympkg.idea;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotificationProvider;

import javax.swing.*;
import java.util.function.Function;

/**
 * Shows a notification bar when editing package.toml,
 * offering to refresh the project (run ymc idea).
 */
public class YummyNotificationProvider implements EditorNotificationProvider, DumbAware {

    @Override
    public Function<? super FileEditor, ? extends JComponent> collectNotificationData(Project project, VirtualFile file) {
        if (!"package.toml".equals(file.getName())) return null;

        return fileEditor -> {
            EditorNotificationPanel panel = new EditorNotificationPanel();
            panel.setText("Yummy: package.toml changed. Refresh project to sync dependencies.");
            panel.createActionLabel("Refresh", () ->
                    YummyService.getInstance(project).runYmcIdea());
            return panel;
        };
    }
}
