package com.foilen.studies.controllers;

import com.azure.core.annotation.QueryParam;
import com.foilen.studies.controllers.models.UserDetailsSingleResult;
import com.foilen.studies.managers.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserManager userManager;

    @GetMapping("/")
    public UserDetailsSingleResult userDetails(Authentication authentication) {
        var result = new UserDetailsSingleResult();
        var userDetails = userManager.getOrCreateUser(authentication);
        result.setItem(userDetails);
        return result;
    }

    @GetMapping("/isLoggedIn")
    public boolean isLoggedIn(Authentication authentication) {
        return authentication != null;
    }

    @GetMapping("/needsLogin")
    public void needsLogin(
            HttpServletResponse response,
            @QueryParam("hash") String hash
    ) throws IOException {
        response.sendRedirect("/#" + hash);
    }

}
