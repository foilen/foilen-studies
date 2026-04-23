package com.foilen.studies.controllers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.DivisionScoresSingleResult;
import com.foilen.studies.controllers.models.RandomDivisionForm;
import com.foilen.studies.controllers.models.RandomDivisionResult;
import com.foilen.studies.controllers.models.TrackDivisionForm;
import com.foilen.studies.managers.DivisionManager;
import com.foilen.studies.managers.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/division")
public class DivisionController {

    @Autowired
    private DivisionManager divisionManager;
    @Autowired
    private UserManager userManager;

    @PostMapping("/random")
    public RandomDivisionResult random(
            Authentication authentication,
            @RequestBody RandomDivisionForm form
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);
        return divisionManager.random(userDetails.getId(), form);
    }

    @GetMapping("/scores")
    public DivisionScoresSingleResult scores(
            Authentication authentication
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);
        return divisionManager.scores(userDetails.getId());
    }

    @PostMapping("/track")
    public FormResult track(
            Authentication authentication,
            @RequestBody TrackDivisionForm form
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);
        return divisionManager.track(userDetails.getId(), form);
    }

}
