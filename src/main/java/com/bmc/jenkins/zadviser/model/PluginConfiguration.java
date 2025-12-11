package com.bmc.jenkins.zadviser.model;

import hudson.Extension;
import hudson.util.Secret;
import jenkins.model.GlobalConfiguration;
import org.kohsuke.stapler.DataBoundSetter;

@Extension
public class PluginConfiguration extends GlobalConfiguration {
    private String username;
    private Secret token;
    private String zAdviserURL;
    private Secret teamHash;

    public PluginConfiguration() {
        load();
    }

    public static PluginConfiguration get() {
        return GlobalConfiguration.all().get(PluginConfiguration.class);
    }

    public String getUsername() {
        return username;
    }

    @DataBoundSetter
    public void setUsername(String username) {
        this.username = username;
        save();
    }

    public Secret getToken() {
        return token;
    }

    @DataBoundSetter
    public void setToken(Secret token) {
        this.token = token;
        save();
    }

    public String getzAdviserURL() {
        return zAdviserURL;
    }

    @DataBoundSetter
    public void setzAdviserURL(String zAdviserURL) {
        this.zAdviserURL = zAdviserURL;
    }

    public Secret getTeamHash() {
        return teamHash;
    }

    @DataBoundSetter
    public void setTeamHash(Secret teamHash) {
        this.teamHash = teamHash;
        save();
    }
}
