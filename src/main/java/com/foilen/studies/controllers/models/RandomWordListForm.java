package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

import java.util.ArrayList;
import java.util.List;

public class RandomWordListForm extends AbstractApiBase {

    private List<RandomWordListParameter> parameters = new ArrayList<>();

    public List<RandomWordListParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<RandomWordListParameter> parameters) {
        this.parameters = parameters;
    }
}
