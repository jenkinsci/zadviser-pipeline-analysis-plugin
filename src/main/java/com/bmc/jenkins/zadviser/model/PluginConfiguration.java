package com.bmc.jenkins.zadviser.model;

import hudson.Extension;
import hudson.util.Secret;
import jenkins.model.GlobalConfiguration;
import org.kohsuke.stapler.DataBoundSetter;

@Extension
public class PluginConfiguration extends GlobalConfiguration {
    private String zAdviserURL;
    private Secret teamHash;

    public PluginConfiguration() {
        load();
    }

    public static PluginConfiguration get() {
        return GlobalConfiguration.all().get(PluginConfiguration.class);
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
