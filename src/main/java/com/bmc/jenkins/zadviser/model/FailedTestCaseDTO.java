package com.bmc.jenkins.zadviser.model;

public class FailedTestCaseDTO {
    private String suiteName;
    private String testName;
    private String errorDetails;
    private String errorStackTrace;
    private String status;

    public FailedTestCaseDTO(
            String suiteName, String testName, String errorDetails, String errorStackTrace, String status) {
        this.suiteName = suiteName;
        this.testName = testName;
        this.errorDetails = errorDetails;
        this.errorStackTrace = errorStackTrace;
        this.status = status;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    public void setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
