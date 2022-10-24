package com.foilen.studies.controllers;

import com.foilen.studies.controllers.models.TextSingleResult;
import com.foilen.studies.managers.AppDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appDetails")
public class AppDetailsController {

    @Autowired
    private AppDetailsManager appDetailsManager;

    @GetMapping("/googleAnalytics")
    public TextSingleResult googleAnalytics() {
        var result = new TextSingleResult();
        var googleAnalyticsId = appDetailsManager.googleAnalyticsGet();
        result.setItem(googleAnalyticsId);
        return result;
    }

}
