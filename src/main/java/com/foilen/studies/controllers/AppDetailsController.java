package com.foilen.studies.controllers;

import com.foilen.studies.controllers.models.TextSingleResult;
import com.foilen.studies.managers.AppDetailsManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appDetails")
public class AppDetailsController {

    @Autowired
    private AppDetailsManager appDetailsManager;
    @Autowired
    private CsrfTokenRepository csrfTokenRepository;

    @GetMapping("/googleAnalytics")
    public TextSingleResult googleAnalytics(HttpServletRequest request, HttpServletResponse response) {
        var result = new TextSingleResult();
        var googleAnalyticsId = appDetailsManager.googleAnalyticsGet();
        result.setItem(googleAnalyticsId);

        // Add the CSRF token if missing
        CsrfToken csrfToken = csrfTokenRepository.loadToken(request);
        if (csrfToken == null) {
            csrfToken = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(csrfToken, request, response);
        }

        return result;
    }

}