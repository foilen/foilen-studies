package com.foilen.studies.scheduledtasks;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.studies.services.SpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanupScheduledTask extends AbstractBasics {

    @Autowired
    private SpeechService speechService;

    @Scheduled(initialDelay = 1000 * 60 * 60, fixedRate = 1000 * 60 * 60 * 24)
    public void cleanupSpeakTextCacheFile() {
        speechService.cleanupSpeakTextCacheFile();
    }

}
