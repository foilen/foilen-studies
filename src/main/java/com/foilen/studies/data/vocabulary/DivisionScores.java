package com.foilen.studies.data.vocabulary;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DivisionScores {

    @Id
    private String userId;

    // 0/1: red
    // 2/3: yellow
    // 4/5: green
    private Short[][] scores = new Short[12][12];

    public String getUserId() {
        return userId;
    }

    public DivisionScores setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Short[][] getScores() {
        return scores;
    }

    public DivisionScores setScores(Short[][] scores) {
        this.scores = scores;
        return this;
    }

}
