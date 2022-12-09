package com.foilen.studies.data.verb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Verb {

    @Id
    private String id;

    private String ownerUserId;

    private String name;
    private List<VerbLine> verbLines;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VerbLine> getVerbLines() {
        return verbLines;
    }

    public void setVerbLines(List<VerbLine> verbLines) {
        this.verbLines = verbLines;
    }
}
