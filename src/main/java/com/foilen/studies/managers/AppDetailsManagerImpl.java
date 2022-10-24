package com.foilen.studies.managers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppDetailsManagerImpl implements AppDetailsManager {

    @Value("${app.googleAnalyticsId:#{null}}")
    private String googleAnalyticsId;

    @Override
    public String googleAnalyticsGet() {
        return googleAnalyticsId;
    }

}
