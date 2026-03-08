package com.ympkg.idea;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Tool window providing quick access to ym/ymc commands.
 */
public class YummyToolWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton buildBtn = new JButton("Build");
        buildBtn.addActionListener(e -> YummyService.getInstance(project).runYmcBuild());

        JButton devBtn = new JButton("Dev");
        devBtn.addActionListener(e -> YummyService.getInstance(project).runYmcDev());

        JButton testBtn = new JButton("Test");
        testBtn.addActionListener(e -> YummyService.getInstance(project).runYmcTest());

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> YummyService.getInstance(project).runYmcIdea());

        buttonPanel.add(buildBtn);
        buttonPanel.add(devBtn);
        buttonPanel.add(testBtn);
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel);

        Content content = ContentFactory.getInstance().createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
