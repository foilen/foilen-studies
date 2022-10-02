package com.foilen.studies.data.vocabulary;

import com.foilen.smalltools.hash.HashSha256;

public class SpeakText {

    private Language language;
    private String text;
    private String cacheId; // TODO Task to update cacheId when null

    public SpeakText() {
    }

    public SpeakText(Language language, String text) {
        this.language = language;
        this.text = text;
        computeCacheId();
    }

    public void computeCacheId() {
        cacheId = HashSha256.hashString(language.name() + "|" + text);
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }
}