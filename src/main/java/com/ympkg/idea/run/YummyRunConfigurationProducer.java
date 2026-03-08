package com.ympkg.idea.run;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;

import java.io.File;

/**
 * Auto-creates run configurations for ymc commands
 * when a package.toml is present in the project.
 */
public class YummyRunConfigurationProducer extends LazyRunConfigurationProducer<YummyRunConfiguration> {

    @Override
    public ConfigurationFactory getConfigurationFactory() {
        return new YummyConfigurationType().getConfigurationFactories()[0];
    }

    @Override
    protected boolean setupConfigurationFromContext(
            YummyRunConfiguration configuration,
            ConfigurationContext context,
            Ref<PsiElement> sourceElement) {

        String basePath = context.getProject().getBasePath();
        if (basePath == null) return false;

        File packageToml = new File(basePath, "package.toml");
        if (!packageToml.exists()) return false;

        configuration.setName("ymc build");
        configuration.command = "build";
        return true;
    }

    @Override
    public boolean isConfigurationFromContext(
            YummyRunConfiguration configuration,
            ConfigurationContext context) {
        return "build".equals(configuration.command);
    }
}
