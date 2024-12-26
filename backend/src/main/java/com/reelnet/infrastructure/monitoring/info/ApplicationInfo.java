package com.reelnet.infrastructure.monitoring.info;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class ApplicationInfo {

    private final BuildProperties buildProperties;

    public ApplicationInfo(BuildProperties buildProperties) {
        this.buildProperties = buildProperties != null ? buildProperties : createDefaultBuildProperties();
    }

    private BuildProperties createDefaultBuildProperties() {
        Properties props = new Properties();
        props.setProperty("version", "dev");
        props.setProperty("artifact", "ReelNet");
        props.setProperty("name", "ReelNet");
        props.setProperty("group", "com.reelnet");
        props.setProperty("time", String.valueOf(System.currentTimeMillis()));
        return new BuildProperties(props);
    }

    public String getVersion() {
        return buildProperties.getVersion();
    }

    public String getName() {
        return buildProperties.getName();
    }

    public String getArtifact() {
        return buildProperties.getArtifact();
    }

    public String getGroup() {
        return buildProperties.getGroup();
    }

    public String getBuildTime() {
        return buildProperties.getTime().toString();
    }
} 