package com.bmc.jenkins.zadviser.model;

import com.cloudbees.workflow.rest.external.StageNodeExt;
import hudson.model.Cause;
import hudson.model.User;
import hudson.scm.ChangeLogSet;
import java.util.List;
import java.util.Set;

public class CombinedRunData {
    private String fullDisplayName;
    private String description;
    private String displayName;
    private String url;
    private String teamHash;
    private String name;
    private String id;
    private String result;

    private List<Cause> causes;
    private List<ChangeLogSet<? extends ChangeLogSet.Entry>> changeSets;
    private Set<User> culprits;

    private Long estimatedDuration;
    private Long queueId;
    private Long timestamp;
    private Long duration;

    private Integer number;

    private List<StageNodeExt> stageData;
    private String testData;

    public CombinedRunData() {}

    public String getFullDisplayName() {
        return fullDisplayName;
    }

    public void setFullDisplayName(String fullDisplayName) {
        this.fullDisplayName = fullDisplayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Cause> getCauses() {
        return causes;
    }

    public void setCauses(List<Cause> causes) {
        this.causes = causes;
    }

    public List<ChangeLogSet<? extends ChangeLogSet.Entry>> getChangeSets() {
        return changeSets;
    }

    public void setChangeSets(List<ChangeLogSet<? extends ChangeLogSet.Entry>> changeSets) {
        this.changeSets = changeSets;
    }

    public Set<User> getCulprits() {
        return culprits;
    }

    public void setCulprits(Set<User> culprits) {
        this.culprits = culprits;
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

    public String getTestData() {
        return testData;
    }

    public void setTestData(String testData) {
        this.testData = testData;
    }
}
