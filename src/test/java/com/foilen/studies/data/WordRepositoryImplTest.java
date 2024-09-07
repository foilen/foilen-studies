package com.foilen.studies.data;

import com.foilen.studies.StudiesApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest(classes = {StudiesApplication.class})
@ActiveProfiles("test")
class WordRepositoryImplTest {

    @Autowired
    WordRepository wordRepository;

    @Test
    void findAllWithSpeakTextSameAsWord() {
        // Check there are some words
        Assertions.assertTrue(wordRepository.count() > 0);

        // Get the words to check
        AtomicLong count = new AtomicLong();
        wordRepository.findAllWithSpeakTextSameAsWord().forEach(word -> {
            count.incrementAndGet();
            Assertions.assertEquals(word.getSpeakText().getText(), word.getWord());
        });

        // Ensure more than 1
        Assertions.assertTrue(count.get() > 1);

    }

}