package com.foilen.studies.managers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.MultiplicationScoresSingleResult;
import com.foilen.studies.controllers.models.RandomMultiplicationForm;
import com.foilen.studies.controllers.models.RandomMultiplicationResult;
import com.foilen.studies.controllers.models.TrackMultiplicationForm;

public interface MultiplicationManager {

    RandomMultiplicationResult random(String userId, RandomMultiplicationForm form);

    MultiplicationScoresSingleResult scores(String userId);

    FormResult track(String userId, TrackMultiplicationForm form);

}
