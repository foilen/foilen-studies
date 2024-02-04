package com.foilen.studies.controllers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.MultiplicationScoresSingleResult;
import com.foilen.studies.controllers.models.RandomMultiplicationForm;
import com.foilen.studies.controllers.models.RandomMultiplicationResult;
import com.foilen.studies.controllers.models.TrackMultiplicationForm;
import com.foilen.studies.managers.MultiplicationManager;
import com.foilen.studies.managers.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/multiplication")
public class MultiplicationController {

    @Autowired
    private UserManager userManager;
    @Autowired
    private MultiplicationManager multiplicationManager;


    @PostMapping("/random")
    public RandomMultiplicationResult random(
            Authentication authentication,
            @RequestBody RandomMultiplicationForm form
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);
        return multiplicationManager.random(userDetails.getId(), form);
    }

    @GetMapping("/scores")
    public MultiplicationScoresSingleResult scores(
            Authentication authentication
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);
        return multiplicationManager.scores(userDetails.getId());
    }

    @PostMapping("/track")
    public FormResult track(
            Authentication authentication,
            @RequestBody TrackMultiplicationForm form
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);
        return multiplicationManager.track(userDetails.getId(), form);
    }

}
