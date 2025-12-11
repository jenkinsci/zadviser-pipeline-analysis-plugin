package com.bmc.jenkins.zadviser.exceptions;

public class MissingDataException extends Exception {
    public MissingDataException(String dataObject) {
        super("Missing data for: " + dataObject);
    }
}
