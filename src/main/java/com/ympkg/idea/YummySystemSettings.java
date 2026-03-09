package com.ympkg.idea;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.externalSystem.settings.AbstractExternalSystemSettings;
import com.intellij.openapi.externalSystem.settings.ExternalSystemSettingsListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;

import java.util.Collections;
import java.util.Set;

@State(name = "YummySettings", storages = @Storage("yummy.xml"))
public class YummySystemSettings
        extends AbstractExternalSystemSettings<YummySystemSettings, YummyProjectSettings, YummySettingsListener>
        implements PersistentStateComponent<YummySystemSettings.MyState> {

    public static final Topic<YummySettingsListener> TOPIC =
            new Topic<>(YummySettingsListener.class, Topic.BroadcastDirection.NONE);

    private MyState myState = new MyState();

    public YummySystemSettings(Project project) {
        super(TOPIC, project);
    }

    public static YummySystemSettings getInstance(Project project) {
        return project.getService(YummySystemSettings.class);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void subscribe(ExternalSystemSettingsListener<YummyProjectSettings> listener) {
    }

    @Override
    protected void copyExtraSettingsFrom(YummySystemSettings settings) {
    }

    @Override
    protected void checkSettings(YummyProjectSettings old, YummyProjectSettings current) {
    }

    @Override
    public MyState getState() {
        MyState state = new MyState();
        fillState(state);
        return state;
    }

    @Override
    public void loadState(MyState state) {
        super.loadState(state);
        myState = state;
    }

    public static class MyState
            implements AbstractExternalSystemSettings.State<YummyProjectSettings> {

        private Set<YummyProjectSettings> myLinkedExternalProjectsSettings = Collections.emptySet();

        @Override
        public Set<YummyProjectSettings> getLinkedExternalProjectsSettings() {
            return myLinkedExternalProjectsSettings;
        }

        @Override
        public void setLinkedExternalProjectsSettings(Set<YummyProjectSettings> settings) {
            myLinkedExternalProjectsSettings = settings;
        }
    }
}
