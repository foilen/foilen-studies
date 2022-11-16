package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class BulkSplitForm extends AbstractApiBase {

    private String all;
    private boolean acceptSpacesInWords;

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public boolean isAcceptSpacesInWords() {
        return acceptSpacesInWords;
    }

    public void setAcceptSpacesInWords(boolean acceptSpacesInWords) {
        this.acceptSpacesInWords = acceptSpacesInWords;
    }
}
