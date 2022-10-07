package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class RandomWordListParameter extends AbstractApiBase {

    private String wordListId;
    private Integer anyScoreCount;

    public String getWordListId() {
        return wordListId;
    }

    public void setWordListId(String wordListId) {
        this.wordListId = wordListId;
    }

    public Integer getAnyScoreCount() {
        return anyScoreCount;
    }

    public void setAnyScoreCount(Integer anyScoreCount) {
        this.anyScoreCount = anyScoreCount;
    }
}
