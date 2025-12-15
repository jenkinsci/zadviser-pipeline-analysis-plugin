package com.bmc.jenkins.zadviser.model;

import com.cloudbees.workflow.rest.external.StageNodeExt;
import java.util.List;

public class CombinedRunData {
    private String fullDisplayName;
    private String displayName;
    private String url;
    private String teamHash;
    private String id;
    private String result;

    private List<String> causes;
    private List<ChangeSetDTO> changeSet;

    private Long estimatedDuration;
    private Long queueId;
    private Long timestamp;
    private Long duration;

    private Integer number;

    private List<StageNodeExt> stageData;

    private int passCount;
    private int failCount;
    private int skipCount;
    private int totalCount;
    private List<FailedTestCaseDTO> failedTestCases;

    public CombinedRunData() {}

    public String getFullDisplayName() {
        return fullDisplayName;
    }

    public void setFullDisplayName(String fullDisplayName) {
        this.fullDisplayName = fullDisplayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTeamHash() {
        return teamHash;
    }

    public void setTeamHash(String teamHash) {
        this.teamHash = teamHash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    public List<ChangeSetDTO> getChangeSet() {
        return changeSet;
    }

    public void setChangeSet(List<ChangeSetDTO> changeSet) {
        this.changeSet = changeSet;
    }

    public Long getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Long estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Long getQueueId() {
        return queueId;
    }

    public void setQueueId(Long queueId) {
        this.queueId = queueId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<StageNodeExt> getStageData() {
        return stageData;
    }

    public void setStageData(List<StageNodeExt> stageData) {
        this.stageData = stageData;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<FailedTestCaseDTO> getFailedTestCases() {
        return failedTestCases;
    }

    public void setFailedTestCases(List<FailedTestCaseDTO> failedTestCases) {
        this.failedTestCases = failedTestCases;
    }
}
