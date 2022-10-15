package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class Scores  extends AbstractApiBase {

    private int total;

    private int good;
    private int average;
    private int bad;
    private int noScore;

    public void addGood() {
        good++;
        total++;
    }

    public void addAverage() {
        average++;
        total++;
    }

    public void addBad() {
        bad++;
        total++;
    }

    public void addNoScore() {
        noScore++;
        total++;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public int getBad() {
        return bad;
    }

    public void setBad(int bad) {
        this.bad = bad;
    }

    public int getNoScore() {
        return noScore;
    }

    public void setNoScore(int noScore) {
        this.noScore = noScore;
    }

    public int getGoodPercentage() {
        return (int) Math.round(100.0 * good / total);
    }

    public int getAveragePercentage() {
        return (int) Math.round(100.0 * average / total);
    }

    public int getBadPercentage() {
        return (int) Math.round(100.0 * bad / total);
    }

    public int getNoScorePercentage() {
        return (int) Math.round(100.0 * noScore / total);
    }

}
