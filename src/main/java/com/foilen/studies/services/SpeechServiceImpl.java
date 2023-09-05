package com.foilen.studies.services;

import com.foilen.smalltools.tools.*;
import com.foilen.studies.data.SpeakTextCacheFileRepository;
import com.foilen.studies.data.VerbRepository;
import com.foilen.studies.data.WordRepository;
import com.foilen.studies.data.vocabulary.Language;
import com.foilen.studies.data.vocabulary.SpeakText;
import com.foilen.studies.data.vocabulary.SpeakTextCacheFile;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class SpeechServiceImpl extends AbstractBasics implements SpeechService {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SpeakTextCacheFileRepository speakTextCacheFileRepository;
    @Autowired
    private VerbRepository verbRepository;
    @Autowired
    private WordRepository wordRepository;

    @Value("${google.tts.userjson:#{null}}")
    private String userjson;

    private TextToSpeechSettings settings;

    @PostConstruct
    public void init() throws IOException {

        if (userjson == null) {
            logger.warn("No google.tts.userjson defined. Will not be able to use Google TTS");
            return;
        }

        // Check if it is a file on the working directory
        InputStream userJsonStream;
        if (FileTools.exists(userjson)) {
            logger.info("Get the Google user in file {}", userjson);
            userJsonStream = new FileInputStream(userjson);
        } else {
            logger.info("Get the Google user in resource {}", userjson);
            userJsonStream = ResourceTools.getResourceAsStream(userjson);
        }

        settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(userJsonStream)))
                .build();

        CloseableTools.close(userJsonStream);
    }

    @Override
    public Resource getFile(String cacheId) {

        // Find get the existing file
        var speakTextCacheFile = speakTextCacheFileRepository.findByCacheId(cacheId);
        if (speakTextCacheFile == null) {

            AssertTools.assertNotNull(settings, "Google TTS is disabled");

            // Find the text to generate
            SpeakText speakText = null;
            var word = wordRepository.findFirstBySpeakTextCacheId(cacheId);
            if (word == null) {
                var verb = verbRepository.findFirstByVerbLinesSpeakTextCacheId(cacheId);
                if (verb != null) {
                    speakText = verb.getVerbLines().stream().filter(v -> v.getSpeakText().getCacheId().equals(cacheId)).findFirst().get().getSpeakText();
                }
            } else {
                speakText = word.getSpeakText();
            }
            if (speakText == null) {
                throw new ResponseStatusException(NOT_FOUND, "No Word or Verb with that speech id");
            }

            // Generate
            var fileContent = generateFile(speakText.getLanguage(), speakText.getText());
            speakTextCacheFile = new SpeakTextCacheFile();
            speakTextCacheFile.setCacheId(cacheId);
            speakTextCacheFile.setMp3Bytes(fileContent);
            speakTextCacheFile = speakTextCacheFileRepository.save(speakTextCacheFile);
        }

        return new ByteArrayResource(speakTextCacheFile.getMp3Bytes());
    }

    @Override
    public void cleanupSpeakTextCacheFile() {
        logger.info("Cleanup SpeakTextCacheFile where cacheId is no more used by any Word");

        TypedAggregation<SpeakTextCacheFile> aggregation = newAggregation(SpeakTextCacheFile.class,
                lookup("word", "cacheId", "speakText.cacheId", "words"),
                match(where("words").size(0)),
                project("_id")
        );
        BufferBatchesTools.<String>autoClose(100,
                idsToDelete -> {
                    var deleteResult = mongoOperations.remove(new Query(where("id").in(idsToDelete)), SpeakTextCacheFile.class);
                    logger.info("Deleted {} SpeakTextCacheFile", deleteResult.getDeletedCount());
                },
                list -> {
                    try (var speakTextCacheFileCloseableIterator = mongoOperations.aggregateStream(aggregation, SpeakTextCacheFile.class)) {
                        speakTextCacheFileCloseableIterator.forEach(speakTextCacheFile -> {
                            logger.info("Will delete SpeakTextCacheFile {}", speakTextCacheFile.getId());
                            list.add(speakTextCacheFile.getId());
                        });
                    }
                }
        );

        logger.info("Cleanup SpeakTextCacheFile done");
    }

    private byte[] generateFile(Language language, String text) {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            String languageCode = switch (language) {
                case EN -> "en-US";
                case FR -> "fr-CA";
            };

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(languageCode)
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();
            AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();

            return audioContents.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
