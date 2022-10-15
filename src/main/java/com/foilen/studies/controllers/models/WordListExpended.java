package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

import java.util.ArrayList;
import java.util.List;

public class WordListExpended extends AbstractApiBase {

    private String id;
    private String name;

    private String ownerUserId;
    private List<WordWithScore> words = new ArrayList<>();

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

    public List<WordWithScore> getWords() {
        return words;
    }

    public void setWords(List<WordWithScore> words) {
        this.words = words;
    }
}
