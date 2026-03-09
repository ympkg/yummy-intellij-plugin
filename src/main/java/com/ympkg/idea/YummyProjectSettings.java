package com.ympkg.idea;

import com.intellij.openapi.externalSystem.settings.ExternalProjectSettings;

public class YummyProjectSettings extends ExternalProjectSettings {

    @Override
    public ExternalProjectSettings clone() {
        YummyProjectSettings copy = new YummyProjectSettings();
        copyTo(copy);
        return copy;
    }
}
