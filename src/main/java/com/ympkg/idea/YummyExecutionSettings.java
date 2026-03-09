package com.ympkg.idea;

import com.intellij.openapi.externalSystem.model.settings.ExternalSystemExecutionSettings;

public class YummyExecutionSettings extends ExternalSystemExecutionSettings {

    private String ymExecutable = "ym";

    public String getYmExecutable() {
        return ymExecutable;
    }

    public void setYmExecutable(String ymExecutable) {
        this.ymExecutable = ymExecutable;
    }
}
