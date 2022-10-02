package com.foilen.studies.data.vocabulary;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class WordList {

    @Id
    private String id;
    private String name;

    private String ownerUserId;
    private List<String> wordIds = new ArrayList<>();

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
}
