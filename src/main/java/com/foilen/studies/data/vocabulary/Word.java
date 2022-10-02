package com.foilen.studies.data.vocabulary;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Word {

    @Id
    private String id;

    private String ownerUserId;

    private String word;
    private SpeakText speakText;

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
}
