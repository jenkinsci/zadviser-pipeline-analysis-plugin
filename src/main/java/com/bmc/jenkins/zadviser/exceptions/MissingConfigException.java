package com.bmc.jenkins.zadviser.exceptions;

public class MissingConfigException extends Exception {
    public MissingConfigException(String dataObject) {
        super("Missing configuration value: " + dataObject);
    }
}
