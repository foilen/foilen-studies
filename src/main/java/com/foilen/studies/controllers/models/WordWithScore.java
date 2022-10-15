package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;
import com.foilen.studies.data.vocabulary.SpeakText;

public class WordWithScore extends AbstractApiBase {

    private String id;

    private String ownerUserId;

    private String word;
    private SpeakText speakText;

    private int score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public SpeakText getSpeakText() {
        return speakText;
    }

    public void setSpeakText(SpeakText speakText) {
        this.speakText = speakText;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
