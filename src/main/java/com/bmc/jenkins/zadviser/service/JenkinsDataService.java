package com.bmc.jenkins.zadviser.service;

import com.bmc.jenkins.zadviser.exceptions.JenkinsResponseException;
import com.bmc.jenkins.zadviser.exceptions.MissingDataException;
import com.bmc.jenkins.zadviser.model.CombinedRunData;
import com.cloudbees.workflow.rest.external.RunExt;
import hudson.model.Run;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

public class JenkinsDataService {
    public static CombinedRunData getJenkinsData(
            Run<?, ?> run, String jenkinsUsername, String jenkinsToken, String teamHash)
            throws IOException, JenkinsResponseException, MissingDataException {
        Jenkins jenkinsInstance = Jenkins.getInstanceOrNull();
        if (jenkinsInstance == null) throw new MissingDataException("Jenkins Instance Setup");
        String jenkinsUrl = jenkinsInstance.getRootUrl();

        String jobName = run.getParent().getName().replace(" ", "%20");
        int buildNumber = run.getNumber();

        String testReportUrl = jenkinsUrl + "job/" + jobName + "/" + buildNumber + "/testReport/api/json";

        Optional<String> testResult = callJenkinsAPI(testReportUrl, jenkinsUsername, jenkinsToken);

        // Populate one big happy object with all the details
        // required including build and stage data
        // test data is excluded from this to reduce enforcing
        // of the JUnit plugin
        RunExt stageData = RunExt.create((WorkflowRun) run);

        return getCombinedRunData(teamHash, run, stageData, testResult.orElse(null));
    }

    private static CombinedRunData getCombinedRunData(
            String teamHash, Run<?, ?> run, RunExt stageData, String testResult) {
        CombinedRunData data = new CombinedRunData();
        data.setTeamHash(teamHash);
        data.setFullDisplayName(run.getFullDisplayName());
        data.setCauses(run.getCauses());
        data.setDescription(run.getDescription());
        data.setDisplayName(run.getDisplayName());
        data.setUrl(run.getUrl());
        data.setChangeSets(((WorkflowRun) run).getChangeSets());
        data.setCulprits(((WorkflowRun) run).getCulprits());
        data.setEstimatedDuration(run.getEstimatedDuration());
        data.setNumber(run.getNumber());
        data.setQueueId(run.getQueueId());
        data.setTimestamp(run.getTimestamp().getTimeInMillis());

        data.setName(stageData.getName());
        data.setId(stageData.getId());
        data.setResult(Objects.requireNonNull(run.getResult()).toString());
        data.setDuration(stageData.getDurationMillis());

        data.setStageData(stageData.getStages());

        data.setTestData(testResult);
        return data;
    }

    private static Optional<String> callJenkinsAPI(String apiUrl, String jenkinsUser, String jenkinsToken)
            throws IOException, JenkinsResponseException {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        String auth =
                Base64.getEncoder().encodeToString((jenkinsUser + ":" + jenkinsToken).getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + auth);
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
