package com.bmc.jenkins.zadviser.model;

public class FailedTestCaseDTO {
    public String suiteName;
    public String testName;
    public String errorDetails;
    public String errorStackTrace;
    public String status;

    public FailedTestCaseDTO(
            String suiteName, String testName, String errorDetails, String errorStackTrace, String status) {
        this.suiteName = suiteName;
        this.testName = testName;
        this.errorDetails = errorDetails;
        this.errorStackTrace = errorStackTrace;
        this.status = status;
    }
}
