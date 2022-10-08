package com.foilen.studies.data.vocabulary;

import java.util.Date;

public class ScoreItem {

    private boolean success;
    private String errorAnswer;

    private Date timestamp;

    public ScoreItem() {
    }

    public ScoreItem(boolean success, String errorAnswer) {
        this.success = success;
        if (!success) {
            this.errorAnswer = errorAnswer;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorAnswer() {
        return errorAnswer;
    }

    public void setErrorAnswer(String errorAnswer) {
        this.errorAnswer = errorAnswer;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
