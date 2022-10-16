package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class RandomWordListParameter extends AbstractApiBase {

    private String wordListId;
    private int anyScoreCount;
    private int noScoreCount;
    private int badScoreCount;
    private int averageScoreCount;
    private int goodScoreCount;

    public String getWordListId() {
        return wordListId;
    }

    public void setWordListId(String wordListId) {
        this.wordListId = wordListId;
    }

    public int getAnyScoreCount() {
        return anyScoreCount;
    }

    public void setAnyScoreCount(int anyScoreCount) {
        this.anyScoreCount = anyScoreCount;
    }

    public int getNoScoreCount() {
        return noScoreCount;
    }

    public void setNoScoreCount(int noScoreCount) {
        this.noScoreCount = noScoreCount;
    }

    public int getBadScoreCount() {
        return badScoreCount;
    }

    public void setBadScoreCount(int badScoreCount) {
        this.badScoreCount = badScoreCount;
    }

    public int getAverageScoreCount() {
        return averageScoreCount;
    }

    public void setAverageScoreCount(int averageScoreCount) {
        this.averageScoreCount = averageScoreCount;
    }

    public int getGoodScoreCount() {
        return goodScoreCount;
    }

    public void setGoodScoreCount(int goodScoreCount) {
        this.goodScoreCount = goodScoreCount;
    }
}
