package com.bmc.jenkins.zadviser.exceptions;

public class JenkinsResponseException extends Exception {
    private final int statusCode;
    private final String responseBody;

    public JenkinsResponseException(int statusCode, String responseBody) {
        super("HTTP " + statusCode + ": " + responseBody);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
