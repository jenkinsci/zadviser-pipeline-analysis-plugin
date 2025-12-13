package com.bmc.jenkins.zadviser.service;

import com.bmc.jenkins.zadviser.exceptions.MissingDataException;
import com.bmc.jenkins.zadviser.model.CombinedRunData;
import com.cloudbees.workflow.rest.external.RunExt;
import hudson.ProxyConfiguration;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

public class JenkinsDataService {
    private static final Logger LOGGER = Logger.getLogger(JenkinsDataService.class.getName());

    public static CombinedRunData getJenkinsData(
            Run<?, ?> run, String jenkinsUsername, String jenkinsToken, String teamHash, TaskListener listener)
            throws MissingDataException {
        Jenkins jenkinsInstance = Jenkins.getInstanceOrNull();
        if (jenkinsInstance == null) throw new MissingDataException("Jenkins Instance Setup");
        String jenkinsUrl = jenkinsInstance.getRootUrl();

        String jobName = run.getParent().getName().replace(" ", "%20");
        int buildNumber = run.getNumber();

        String testReportUrl = jenkinsUrl + "job/" + jobName + "/" + buildNumber + "/testReport/api/json";

        String testResult = callJenkinsAPI(testReportUrl, jenkinsUsername, jenkinsToken, listener);

        // Populate one big happy object with all the details
        // required including build and stage data
        // test data is excluded from this to reduce enforcing
        // of the JUnit plugin
        RunExt stageData = RunExt.create((WorkflowRun) run);

        return getCombinedRunData(teamHash, run, stageData, testResult);
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

    private static String callJenkinsAPI(
            String apiUrl, String jenkinsUser, String jenkinsToken, TaskListener listener) {
        try {
            HttpClient client = ProxyConfiguration.newHttpClientBuilder().build();

            String auth = Base64.getEncoder()
                    .encodeToString((jenkinsUser + ":" + jenkinsToken).getBytes(StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .header("Authorization", "Basic " + auth)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) return response.body();
            else if (response.statusCode() == 403) {
                listener.getLogger()
                        .println(
                                "[zAdviser Pipeline Analysis Plugin]: Jenkins server status 403 while trying to fetch test data, please check your auth credentials, Jenkins URL, and proxy setup");
            }
            return null;
        } catch (Exception e) {
            listener.getLogger().println("[zAdviser Pipeline Analysis Plugin]: Error fetching test data: " + e);
            LOGGER.log(Level.WARNING, "[zAdviser Pipeline Analysis Plugin] Unknown error stack trace: ", e);
            return null;
        }
    }
}
