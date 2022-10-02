package com.foilen.studies.services;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.ResourceTools;
import com.foilen.studies.data.SpeakTextCacheRepository;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class SpeechServiceImpl extends AbstractBasics implements SpeechService {

    @Autowired
    private SpeakTextCacheRepository speakTextCacheRepository;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(ResourceTools.getResourceAsStream("/google-service-account.json"))))
                .build();
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText("Bonjour mon grand").build();

            // Build the voice request, select the language code ("en-US") and the ssml voice gender
            // ("neutral")
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("fr-CA")
                            .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                            .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            // Perform the text-to-speech request on the text input with the selected voice parameters and
            // audio file type
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            // Write the response to the output file.
            try (OutputStream out = new FileOutputStream("output.mp3")) {
                out.write(audioContents.toByteArray());
                System.out.println("Audio content written to file \"output.mp3\"");
            }
        }
    }

}
