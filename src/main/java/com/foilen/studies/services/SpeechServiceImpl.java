package com.foilen.studies.services;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.CloseableTools;
import com.foilen.smalltools.tools.FileTools;
import com.foilen.smalltools.tools.ResourceTools;
import com.foilen.studies.data.SpeakTextCacheFileRepository;
import com.foilen.studies.data.WordRepository;
import com.foilen.studies.data.vocabulary.Language;
import com.foilen.studies.data.vocabulary.SpeakTextCacheFile;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class SpeechServiceImpl extends AbstractBasics implements SpeechService {

    @Autowired
    private SpeakTextCacheFileRepository speakTextCacheFileRepository;
    @Autowired
    private WordRepository wordRepository;

    @Value("${google.tts.userjson}")
    private String userjson;

    private TextToSpeechSettings settings;

    @PostConstruct
    public void init() throws IOException {

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
            // Find the text to generate
            var word = wordRepository.findOneBySpeakTextCacheId(cacheId);
            if (word == null) {
                throw new ResponseStatusException(NOT_FOUND, "No Word with that speech id");
            }

            // Generate
            var fileContent = generateFile(word.getSpeakText().getLanguage(), word.getSpeakText().getText());
            speakTextCacheFile = new SpeakTextCacheFile();
            speakTextCacheFile.setCacheId(cacheId);
            speakTextCacheFile.setMp3Bytes(fileContent);
            speakTextCacheFile = speakTextCacheFileRepository.save(speakTextCacheFile);
        }

        return new ByteArrayResource(speakTextCacheFile.getMp3Bytes());
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
