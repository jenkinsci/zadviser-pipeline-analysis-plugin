package com.bmc.jenkins.zadviser.service;

import com.bmc.jenkins.zadviser.exceptions.MissingDataException;
import com.bmc.jenkins.zadviser.model.ChangeSetDTO;
import com.bmc.jenkins.zadviser.model.CombinedRunData;
import com.bmc.jenkins.zadviser.model.FailedTestCaseDTO;
import com.cloudbees.workflow.rest.external.RunExt;
import hudson.PluginWrapper;
import hudson.model.Cause;
import hudson.model.Run;
import hudson.scm.ChangeLogSet;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.TestResultAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

public class JenkinsDataService {
    public static CombinedRunData getJenkinsData(Run<?, ?> run, String teamHash) throws MissingDataException {
        Jenkins jenkinsInstance = Jenkins.getInstanceOrNull();
        if (jenkinsInstance == null) throw new MissingDataException("Jenkins Instance Setup");

        List<FailedTestCaseDTO> failedTestCases = new ArrayList<>();
        int passCount = 0;
        int failCount = 0;
        int skipCount = 0;
        int totalCount = 0;

        if (isJUnitPluginInstalled()) {
            List<TestResultAction> testResultActions = run.getActions(TestResultAction.class);
            for (TestResultAction action : testResultActions) {
                passCount = passCount + action.getResult().getPassCount();
                failCount = failCount + action.getResult().getFailCount();
                skipCount = skipCount + action.getResult().getSkipCount();
                totalCount = totalCount + action.getResult().getTotalCount();

                for (CaseResult test : action.getFailedTests()) {
                    String suiteName = test.getClassName();
                    String testName = test.getSimpleName();
                    String status = test.getStatus().toString();
                    String errorStackTrace = test.getErrorStackTrace();
                    String errorDetails = test.getErrorDetails();

                    failedTestCases.add(
                            new FailedTestCaseDTO(suiteName, testName, errorDetails, errorStackTrace, status));
                }
            }
        }

        // Populate one big happy object with all the details
        // required including build and stage data
        // test data is excluded from this to reduce enforcing
        // of the JUnit plugin
        RunExt stageData = RunExt.create((WorkflowRun) run);

        return getCombinedRunData(
                teamHash, run, stageData, passCount, failCount, skipCount, totalCount, failedTestCases);
    }

    private static CombinedRunData getCombinedRunData(
            String teamHash,
            Run<?, ?> run,
            RunExt stageData,
            Integer passCount,
            Integer failCount,
            Integer skipCount,
            Integer totalCount,
            List<FailedTestCaseDTO> failedTestCases) {
        CombinedRunData data = new CombinedRunData();
        data.setTeamHash(teamHash);
        data.setFullDisplayName(run.getFullDisplayName());

        List<String> causes = new ArrayList<>();
        for (Cause cause : run.getCauses()) {
            causes.add(cause.getShortDescription());
        }
        data.setCauses(causes);

        data.setDisplayName(run.getDisplayName());
        data.setUrl(run.getUrl());

        List<ChangeSetDTO> changeSet = new ArrayList<>();
        for (ChangeLogSet<? extends ChangeLogSet.Entry> changeSetItem : ((WorkflowRun) run).getChangeSets()) {
            for (ChangeLogSet.Entry item : changeSetItem) {
                changeSet.add(
                        new ChangeSetDTO(item.getCommitId(), item.getAuthor().getFullName(), item.getMsg()));
            }
        }
        data.setChangeSet(changeSet);

        data.setEstimatedDuration(run.getEstimatedDuration());
        data.setNumber(run.getNumber());
        data.setQueueId(run.getQueueId());
        data.setTimestamp(run.getTimestamp().getTimeInMillis());

        data.setId(stageData.getId());
        data.setResult(Objects.requireNonNull(run.getResult()).toString());
        data.setDuration(stageData.getDurationMillis());

        data.setStageData(stageData.getStages());

        data.setPassCount(passCount);
        data.setFailCount(failCount);
        data.setSkipCount(skipCount);
        data.setTotalCount(totalCount);
        data.setFailedTestCases(failedTestCases);
        return data;
    }

    public static boolean isJUnitPluginInstalled() {
        Jenkins jenkins = Jenkins.getInstanceOrNull();
        if (jenkins == null) return false;

        PluginWrapper junit = jenkins.getPluginManager().getPlugin("junit");
        return junit != null && junit.isActive();
    }
}
