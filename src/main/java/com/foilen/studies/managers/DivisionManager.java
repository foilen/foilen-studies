package com.foilen.studies.managers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.DivisionScoresSingleResult;
import com.foilen.studies.controllers.models.RandomDivisionForm;
import com.foilen.studies.controllers.models.RandomDivisionResult;
import com.foilen.studies.controllers.models.TrackDivisionForm;

public interface DivisionManager {

    RandomDivisionResult random(String userId, RandomDivisionForm form);

    DivisionScoresSingleResult scores(String userId);

    FormResult track(String userId, TrackDivisionForm form);

}
