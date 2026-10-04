package com.foilen.studies.controllers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.ChangePasswordForm;
import com.foilen.studies.controllers.models.LoginForm;
import com.foilen.studies.controllers.models.LoginWithCodeForm;
import com.foilen.studies.controllers.models.LoginWithCodeRequestForm;
import com.foilen.studies.controllers.models.UserDetailsSingleResult;
import com.foilen.studies.controllers.models.UserInfo;
import com.foilen.studies.managers.UserManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CsrfTokenRepository csrfTokenRepository;
    @Autowired
    private UserManager userManager;

    @PostMapping("/changePassword")
    public FormResult changePassword(Authentication authentication, @RequestBody ChangePasswordForm form) {
        return userManager.changePassword(authentication, form);
    }

    @GetMapping("/csrf")
    public void csrf(HttpServletRequest request, HttpServletResponse response) {
        var token = csrfTokenRepository.generateToken(request);
        csrfTokenRepository.saveToken(token, request, response);
    }

    @GetMapping("/isLoggedIn")
    public boolean isLoggedIn(Authentication authentication) {
        return authentication != null;
    }

    @PostMapping("/login")
    public FormResult login(@RequestBody LoginForm form, HttpServletRequest request, HttpServletResponse response) {
        return userManager.login(form, request, response);
    }

    @PostMapping("/loginWithCode")
    public FormResult loginWithCode(@RequestBody LoginWithCodeForm form, HttpServletRequest request, HttpServletResponse response) {
        return userManager.loginWithCode(form, request, response);
    }

    @PostMapping("/loginWithCodeRequest")
    public FormResult loginWithCodeRequest(@RequestBody LoginWithCodeRequestForm form) {
        return userManager.loginWithCodeRequest(form);
    }

    @PostMapping("/logout")
    public FormResult logout(HttpServletRequest request, HttpServletResponse response) {
        return userManager.logout(request, response);
    }

    @GetMapping("/")
    public UserDetailsSingleResult userDetails(Authentication authentication) {
        var result = new UserDetailsSingleResult();
        var userDetails = userManager.getOrCreateUser(authentication);
        result.setItem(new UserInfo()
                .setId(userDetails.getId())
                .setEmail(userDetails.getEmail())
                .setPasswordSet(userDetails.getPasswordHash() != null)
                .setCreationDate(userDetails.getCreationDate())
                .setLastLoginDate(userDetails.getLastLoginDate()));
        return result;
    }

}
