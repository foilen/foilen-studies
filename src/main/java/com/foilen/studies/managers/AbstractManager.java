package com.foilen.studies.managers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.CollectionsTools;

public abstract class AbstractManager extends AbstractBasics {

    protected void validateMin(FormResult formResult, String fieldName, int fieldValue, int min) {
        if (fieldValue < min) {
            CollectionsTools.getOrCreateEmptyArrayList(formResult.getValidationErrorsByField(), fieldName, String.class).add("Trop petit. Minimum " + min);
        }
    }

    protected void validateMin(FormResult formResult, String fieldName, short fieldValue, short min) {
        if (fieldValue < min) {
            CollectionsTools.getOrCreateEmptyArrayList(formResult.getValidationErrorsByField(), fieldName, String.class).add("Trop petit. Minimum " + min);
        }
    }

    protected void validateMax(FormResult formResult, String fieldName, int fieldValue, int max) {
        if (fieldValue > max) {
            CollectionsTools.getOrCreateEmptyArrayList(formResult.getValidationErrorsByField(), fieldName, String.class).add("Trop grand. Maximum " + max);
        }
    }

    protected void validateMax(FormResult formResult, String fieldName, short fieldValue, short max) {
        if (fieldValue > max) {
            CollectionsTools.getOrCreateEmptyArrayList(formResult.getValidationErrorsByField(), fieldName, String.class).add("Trop grand. Maximum " + max);
        }
    }

}
