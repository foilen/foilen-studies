package com.foilen.studies.managers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.ChangePasswordForm;
import com.foilen.studies.controllers.models.LoginForm;
import com.foilen.studies.controllers.models.LoginWithCodeForm;
import com.foilen.studies.controllers.models.LoginWithCodeRequestForm;
import com.foilen.studies.data.user.UserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface UserManager {

    FormResult changePassword(Authentication authentication, ChangePasswordForm form);

    /**
     * Get the user of the authentication.
     *
     * @param authentication the authentication (its name is the email of the user)
     * @return the user
     */
    UserDetails getOrCreateUser(Authentication authentication);

    FormResult login(LoginForm form, HttpServletRequest request, HttpServletResponse response);

    FormResult loginWithCode(LoginWithCodeForm form, HttpServletRequest request, HttpServletResponse response);

    FormResult loginWithCodeRequest(LoginWithCodeRequestForm form);

    FormResult logout(HttpServletRequest request, HttpServletResponse response);

}
