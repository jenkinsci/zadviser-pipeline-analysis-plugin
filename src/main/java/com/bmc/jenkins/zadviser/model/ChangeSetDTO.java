package com.bmc.jenkins.zadviser.model;

public class ChangeSetDTO {
    private String commitId;
    private String authorName;
    private String commitMsg;

    public ChangeSetDTO(String commitId, String authorName, String commitMsg) {
        this.commitId = commitId;
        this.authorName = authorName;
        this.commitMsg = commitMsg;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCommitMsg() {
        return commitMsg;
    }

    public void setCommitMsg(String commitMsg) {
        this.commitMsg = commitMsg;
    }
}
