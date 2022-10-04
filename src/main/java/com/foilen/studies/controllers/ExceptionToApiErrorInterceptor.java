package com.foilen.studies.controllers;

import com.foilen.smalltools.restapi.model.ApiError;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.studies.controllers.models.ExceptionErrorResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class ExceptionToApiErrorInterceptor extends AbstractBasics {

    private ExceptionErrorResult log(ExceptionErrorResult result) {
        logger.error("Returning error to user {}", JsonTools.compactPrint(result));
        return result;
    }

    @ExceptionHandler(RuntimeException.class)
    public ExceptionErrorResult handleRuntimeException(RuntimeException ex) {
        return log(new ExceptionErrorResult(new ApiError(ex.getMessage())));
    }

    // TODO Handle ResponseStatusException

}
