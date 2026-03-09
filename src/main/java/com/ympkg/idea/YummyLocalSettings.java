package com.ympkg.idea;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.externalSystem.settings.AbstractExternalSystemLocalSettings;
import com.intellij.openapi.project.Project;

@State(name = "YummyLocalSettings", storages = @Storage("yummy.local.xml"))
public class YummyLocalSettings extends AbstractExternalSystemLocalSettings<AbstractExternalSystemLocalSettings.State> {

    public YummyLocalSettings(Project project) {
        super(YummyConstants.SYSTEM_ID, project);
    }
}
