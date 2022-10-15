package com.foilen.studies.services;

import com.foilen.smalltools.test.asserts.AssertTools;
import com.foilen.studies.StudiesApplication;
import com.foilen.studies.data.SpeakTextCacheFileRepository;
import com.foilen.studies.data.WordRepository;
import com.foilen.studies.fakedata.FakeDataLoader;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Collectors;

@SpringBootTest(classes = {StudiesApplication.class})
@ActiveProfiles("test")
class SpeechServiceImplTest {

    @Autowired
    private FakeDataLoader fakeDataLoader;
    @Autowired
    private SpeakTextCacheFileRepository speakTextCacheFileRepository;
    @Autowired
    private SpeechService speechService;
    @Autowired
    private WordRepository wordRepository;

    @Test
    void cleanupSpeakTextCacheFile() {

        fakeDataLoader.loadFakeData();

        // Those in use
        var allWords = wordRepository.findAll();
        var allSpeakTextCacheFileIds = allWords.stream()
                .map(word -> word.getSpeakText().getCacheId())
                .collect(Collectors.toSet());

        // Ensure at least one has a cached value
        var speakTextCacheFile = speakTextCacheFileRepository.findAllByCacheIdIn(allSpeakTextCacheFileIds);
        Assert.assertFalse("There should be at least one speak text cache file used by a word", speakTextCacheFile.isEmpty());

        // Cleanup
        speechService.cleanupSpeakTextCacheFile();

        // Ensure they all still exist
        var speakTextCacheFileAfter = speakTextCacheFileRepository.findAllByCacheIdIn(allSpeakTextCacheFileIds);
        AssertTools.assertJsonComparison(speakTextCacheFile, speakTextCacheFileAfter);

        // Ensure the unused ones are gone
        var unusedSpeakTextCacheFile = speakTextCacheFileRepository.findAllByCacheIdNotIn(allSpeakTextCacheFileIds);
        Assert.assertTrue("There should be no unused speak text cache file", unusedSpeakTextCacheFile.isEmpty());

    }
}