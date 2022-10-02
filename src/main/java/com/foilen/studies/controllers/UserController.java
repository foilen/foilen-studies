package com.foilen.studies.controllers;

import com.foilen.studies.managers.UserManager;
import com.foilen.studies.controllers.models.UserDetailsSingleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserManager userManager;

    @GetMapping("/user")
    public UserDetailsSingleResult userDetails(Authentication authentication) {
        var result = new UserDetailsSingleResult();
        var userDetails = userManager.getOrCreateUser(authentication);
        result.setItem(userDetails);
        return result;
    }

}
