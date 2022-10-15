package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

import java.util.ArrayList;
import java.util.List;

public class WordListWithScore extends AbstractApiBase {

    private String id;
    private String name;
    private String ownerUserId;
    private List<String> wordIds = new ArrayList<>();

    private Scores scores = new Scores();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public List<String> getWordIds() {
        return wordIds;
    }

    public void setWordIds(List<String> wordIds) {
        this.wordIds = wordIds;
    }

    public Scores getScores() {
        return scores;
    }

    public void setScores(Scores scores) {
        this.scores = scores;
    }
}
