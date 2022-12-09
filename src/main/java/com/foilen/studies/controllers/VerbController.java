package com.foilen.studies.controllers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.VerbListResult;
import com.foilen.studies.controllers.models.VerbResult;
import com.foilen.studies.data.verb.Verb;
import com.foilen.studies.managers.UserManager;
import com.foilen.studies.managers.VerbManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verb")
public class VerbController {

    @Autowired
    private UserManager userManager;
    @Autowired
    private VerbManager verbManager;

    @GetMapping("/")
    public VerbListResult list(Authentication authentication) {
        var userDetails = userManager.getOrCreateUser(authentication);
        return verbManager.listVerb(userDetails.getId());
    }

    @GetMapping("/{verbId}")
    public VerbResult get(Authentication authentication,
                          @PathVariable String verbId
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);
        return verbManager.getVerb(userDetails.getId(), verbId);
    }

    @PostMapping("/")
    public FormResult save(
            Authentication authentication,
            @RequestBody Verb form
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);

        return verbManager.saveVerb(userDetails.getId(), form);
    }

    @DeleteMapping("/{verbId}")
    public FormResult delete(
            Authentication authentication,
            @PathVariable String verbId
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);
        return verbManager.deleteVerb(userDetails.getId(), verbId);
    }

}
