package com.ympkg.idea;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Maps the JSON output of `ym idea --json`.
 */
public class YummyProjectModel {

    private static final Gson GSON = new Gson();

    public String name;
    public String groupId;
    public String version;
    public String jdkVersion;
    public String type; // "single" or "workspace"
    public List<Module> modules;

    public static YummyProjectModel parse(String json) {
        return GSON.fromJson(json, YummyProjectModel.class);
    }

    public static class Module {
        public String name;
        public String path;
        public List<SourceFolder> sourceFolders;
        public String outputPath;
        public String testOutputPath;
        public List<Dependency> dependencies;
        public List<String> annotationProcessors;

        public List<SourceFolder> getSourceFolders() {
            return sourceFolders != null ? sourceFolders : Collections.emptyList();
        }

        public List<Dependency> getDependencies() {
            return dependencies != null ? dependencies : Collections.emptyList();
        }

        public List<String> getAnnotationProcessors() {
            return annotationProcessors != null ? annotationProcessors : Collections.emptyList();
        }
    }

    public static class SourceFolder {
        public String path;
        public String type; // SOURCE, TEST, RESOURCE, TEST_RESOURCE
    }

    public static class Dependency {
        public String type; // "library" or "module"
        public String name;
        public String jarPath;
        public String sourcePath;
        public String scope; // COMPILE, RUNTIME, PROVIDED, TEST
    }
}
