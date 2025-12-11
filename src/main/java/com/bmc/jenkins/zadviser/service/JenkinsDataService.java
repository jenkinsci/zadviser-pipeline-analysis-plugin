package com.bmc.jenkins.zadviser.service;

import com.bmc.jenkins.zadviser.exceptions.JenkinsResponseException;
import com.bmc.jenkins.zadviser.exceptions.MissingDataException;
import com.bmc.jenkins.zadviser.model.JenkinsDataServiceResponse;
import hudson.model.Run;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import org.json.JSONArray;
import org.json.JSONObject;

public class JenkinsDataService {
    private static final Logger LOGGER = Logger.getLogger(JenkinsDataService.class.getName());

    public static JenkinsDataServiceResponse getJenkinsData(
            Run<?, ?> run, String jenkinsUsername, String jenkinsToken, String teamHash)
            throws IOException, JenkinsResponseException, MissingDataException {
        Jenkins jenkinsInstance = Jenkins.getInstanceOrNull();
        if (jenkinsInstance == null) throw new MissingDataException("Jenkins Instance Setup");
        String jenkinsUrl = jenkinsInstance.getRootUrl();

        String jobName = run.getParent().getName().replace(" ", "%20");
        int buildNumber = run.getNumber();

        String apiUrl = jenkinsUrl + "job/" + jobName + "/" + buildNumber + "/api/json";
        String wfApiUrl = jenkinsUrl + "job/" + jobName + "/" + buildNumber + "/wfapi/describe";
        String testReportUrl = jenkinsUrl + "job/" + jobName + "/" + buildNumber + "/testReport/api/json";

        Optional<String> buildData = callJenkinsAPI(apiUrl, jenkinsUsername, jenkinsToken);
        Optional<String> stageDescribe = callJenkinsAPI(wfApiUrl, jenkinsUsername, jenkinsToken);
        Optional<String> testResult = callJenkinsAPI(testReportUrl, jenkinsUsername, jenkinsToken);

        if (buildData.isEmpty()) {
            throw new MissingDataException("Build data");
        }
        if (stageDescribe.isEmpty()) {
            throw new MissingDataException("Stage data");
        }

        JSONArray nodeStageDataArr =
                prepareStageData(stageDescribe.get(), jenkinsUrl, jobName, buildNumber, jenkinsUsername, jenkinsToken);

        return new JenkinsDataServiceResponse(
                teamHash, nodeStageDataArr, buildData.get(), stageDescribe.get(), testResult.orElse(null));
    }

    private static JSONArray prepareStageData(
            String stageDescribe,
            String jenkinsUrl,
            String jobName,
            int buildNumber,
            String jenkinsUser,
            String jenkinsToken)
            throws IOException, JenkinsResponseException, MissingDataException {
        JSONObject wfObj = new JSONObject(stageDescribe);
        JSONArray stages = wfObj.getJSONArray("stages");
        JSONArray nodeStageDataArr = new JSONArray();

        for (int i = 0; i < stages.length(); i++) {
            JSONObject stage = stages.getJSONObject(i);
            String nodeId = stage.getString("id");
            String nodeApiUrl =
                    jenkinsUrl + "job/" + jobName + "/" + buildNumber + "/execution/node/" + nodeId + "/wfapi/describe";
            Optional<String> nodeData = callJenkinsAPI(nodeApiUrl, jenkinsUser, jenkinsToken);
            JSONObject nodeObj = new JSONObject();
            nodeObj.put("nodeId", nodeId);

            if (nodeData.isEmpty()) throw new MissingDataException("Stage" + nodeApiUrl);

            nodeObj.put("data", new JSONObject(nodeData.get()));
            nodeStageDataArr.put(nodeObj);
        }
        return nodeStageDataArr;
    }

    private static Optional<String> callJenkinsAPI(String apiUrl, String jenkinsUser, String jenkinsToken)
            throws IOException, JenkinsResponseException {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        String auth =
                Base64.getEncoder().encodeToString((jenkinsUser + ":" + jenkinsToken).getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + auth);
        LOGGER.log(Level.SEVERE, String.valueOf(conn.getURL()));
        LOGGER.log(Level.SEVERE, jenkinsUser + " " + jenkinsToken);
        return readResponse(conn);
    }

    private static Optional<String> readResponse(HttpURLConnection conn) throws IOException, JenkinsResponseException {
        int status = conn.getResponseCode();
        InputStream inputStream = status >= 400 ? conn.getErrorStream() : conn.getInputStream();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            if (status == 404) return Optional.empty();
            else if (status > 400) throw new JenkinsResponseException(status, response.toString());

            return Optional.of(response.toString());
        }
    }
}
