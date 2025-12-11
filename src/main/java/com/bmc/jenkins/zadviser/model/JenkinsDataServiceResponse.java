package com.bmc.jenkins.zadviser.model;

import org.json.JSONArray;

public record JenkinsDataServiceResponse(
        String teamHash, JSONArray nodeStageDataArray, String buildData, String stageDescribe, String testResult) {}
