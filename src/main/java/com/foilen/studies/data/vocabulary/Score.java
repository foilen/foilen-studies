package com.foilen.studies.data.vocabulary;

import java.util.ArrayList;
import java.util.List;

public class Score {

    private Integer score;
    private List<ScoreItem> lastScoreItems = new ArrayList<>();

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<ScoreItem> getLastScoreItems() {
        return lastScoreItems;
    }

    public void setLastScoreItems(List<ScoreItem> lastScoreItems) {
        this.lastScoreItems = lastScoreItems;
    }
}
