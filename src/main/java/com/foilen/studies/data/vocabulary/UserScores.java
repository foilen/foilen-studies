package com.foilen.studies.data.vocabulary;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

// TODO Scheduled Task - Delete score for Words that doesn't exist anymore for 2 months
@Document
public class UserScores {

    @Id
    private String userId;

    private Map<String, Score> scoreByWordId = new HashMap<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Score> getScoreByWordId() {
        return scoreByWordId;
    }

    public void setScoreByWordId(Map<String, Score> scoreByWordId) {
        this.scoreByWordId = scoreByWordId;
    }
}
