package com.foilen.studies.upgradetasks.tasks;

import com.foilen.smalltools.upgrader.trackers.AbstractMongoUpgradeTask;
import com.foilen.studies.data.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class V_20250426_01_RemoveTooLongSentences extends AbstractMongoUpgradeTask {

    @Autowired
    private WordRepository wordRepository;

    @Override
    public void execute() {
        var mongoDatabase = mongoClient.getDatabase(databaseName);
        var wordCollection = mongoDatabase.getCollection("word");

        // Find words where "speakText.text" is longer than 210 characters
        // Update by putting "word" in "speakText.text"
        wordRepository.findAll().stream()
                .filter(word -> word.getSpeakText().getText().length() > 210)
                .forEach(word -> {
                    logger.info("Updating word {} - {} to have shorter sentence", word.getId(), word.getWord());
                    var speakText = word.getSpeakText();
                    speakText.setText(word.getWord());
                    wordRepository.save(word);
                });
    }

}
