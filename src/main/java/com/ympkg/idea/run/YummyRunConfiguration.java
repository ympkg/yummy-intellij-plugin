package com.ympkg.idea.run;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Run configuration for executing ym commands.
 */
public class YummyRunConfiguration extends RunConfigurationBase<RunProfileState> {

    String command = "build";
    String extraArgs = "";

    YummyRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @Override
    public RunProfileState getState(Executor executor, ExecutionEnvironment environment) {
        return new CommandLineState(environment) {
            @Override
            protected OSProcessHandler startProcess() throws com.intellij.execution.ExecutionException {
                List<String> args = new ArrayList<>();
                args.add(command);
                if (extraArgs != null && !extraArgs.isBlank()) {
                    for (String arg : extraArgs.split(" ")) {
                        args.add(arg);
                    }
                }
                GeneralCommandLine commandLine = new GeneralCommandLine("ym")
                        .withParameters(args)
                        .withWorkDirectory(getProject().getBasePath())
                        .withCharset(java.nio.charset.StandardCharsets.UTF_8);
                return new OSProcessHandler(commandLine);
            }
        };
    }

    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new YummySettingsEditor();
    }
}

class YummySettingsEditor extends SettingsEditor<YummyRunConfiguration> {

    private final JComboBox<String> commandField = new JComboBox<>(
            new String[]{"build", "dev", "test", "build --release"});
    private final JTextField argsField = new JTextField();

    @Override
    protected void resetEditorFrom(YummyRunConfiguration config) {
        commandField.setSelectedItem(config.command);
        argsField.setText(config.extraArgs);
    }

    @Override
    protected void applyEditorTo(YummyRunConfiguration config) {
        config.command = (String) commandField.getSelectedItem();
        config.extraArgs = argsField.getText();
    }

    @Override
    protected JComponent createEditor() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel cmdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmdPanel.add(new JLabel("Command:"));
        cmdPanel.add(commandField);
        panel.add(cmdPanel);

        JPanel argsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        argsPanel.add(new JLabel("Extra args:"));
        argsPanel.add(argsField);
        panel.add(argsPanel);

        return panel;
    }
}
