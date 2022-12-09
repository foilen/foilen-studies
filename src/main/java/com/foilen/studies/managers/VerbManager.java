package com.foilen.studies.managers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.VerbListResult;
import com.foilen.studies.controllers.models.VerbResult;
import com.foilen.studies.data.verb.Verb;

public interface VerbManager {


    VerbListResult listVerb(String userId);

    VerbResult getVerb(String userId, String verbId);

    FormResult saveVerb(String userId, Verb form);

    FormResult deleteVerb(String userId, String verbId);
}
