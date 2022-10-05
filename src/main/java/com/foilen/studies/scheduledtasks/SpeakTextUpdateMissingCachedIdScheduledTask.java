package com.foilen.studies.scheduledtasks;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.studies.data.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpeakTextUpdateMissingCachedIdScheduledTask extends AbstractBasics {

    @Autowired
    private WordRepository wordRepository;

    // Every 5 minutes
    @Scheduled(fixedDelay = 5 * 60000)
    public void update() {

        var all = wordRepository.findAllBySpeakTextCacheIdIsNull();
        all.forEach(word -> {
            logger.info("Updating speak text cache id for word {}", word.getWord());
            word.getSpeakText().computeCacheId();
            wordRepository.save(word);
        });

    }

}
