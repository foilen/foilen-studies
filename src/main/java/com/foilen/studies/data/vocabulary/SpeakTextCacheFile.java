package com.foilen.studies.data.vocabulary;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SpeakTextCacheFile {

    @Id
    private String id;
    private String cacheId;

    private byte[] mp3Bytes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    public byte[] getMp3Bytes() {
        return mp3Bytes;
    }

    public void setMp3Bytes(byte[] mp3Bytes) {
        this.mp3Bytes = mp3Bytes;
    }
}
