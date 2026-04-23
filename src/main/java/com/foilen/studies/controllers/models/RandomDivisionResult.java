package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.FormResult;

import java.util.ArrayList;
import java.util.List;

public class RandomDivisionResult extends FormResult {

    private List<Short[]> questions = new ArrayList<>();

    public List<Short[]> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Short[]> questions) {
        this.questions = questions;
    }

}
