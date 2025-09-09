package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class CopyWordListForm extends AbstractApiBase {

    private String fromWordListId;
    private String toWordListId;

    public String getFromWordListId() {
        return fromWordListId;
    }

    public void setFromWordListId(String fromWordListId) {
        this.fromWordListId = fromWordListId;
    }

    public String getToWordListId() {
        return toWordListId;
    }

    public void setToWordListId(String toWordListId) {
        this.toWordListId = toWordListId;
    }
}
