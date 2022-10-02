package com.foilen.studies.controllers.models;

import com.foilen.smalltools.restapi.model.AbstractApiBaseWithError;
import com.foilen.smalltools.restapi.model.ApiError;

public class ExceptionErrorResult extends AbstractApiBaseWithError {

    public ExceptionErrorResult() {
    }

    public ExceptionErrorResult(ApiError error) {
        setError(error);
    }


}
