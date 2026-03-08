package com.ympkg.idea.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;

import javax.swing.*;

public class YummyConfigurationType implements ConfigurationType {

    @Override
    public String getDisplayName() { return "Yummy"; }

    @Override
    public String getConfigurationTypeDescription() { return "Run ymc commands"; }

    @Override
    public Icon getIcon() { return AllIcons.RunConfigurations.Application; }

    @Override
    public String getId() { return "YummyRunConfiguration"; }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{ new YummyConfigurationFactory(this) };
    }
}

class YummyConfigurationFactory extends ConfigurationFactory {

    YummyConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    public String getId() { return "YummyConfigurationFactory"; }

    @Override
    public RunConfiguration createTemplateConfiguration(Project project) {
        return new YummyRunConfiguration(project, this, "ymc build");
    }
}
