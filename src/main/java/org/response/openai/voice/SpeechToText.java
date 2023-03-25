package org.response.openai.voice;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import io.github.cdimascio.dotenv.Dotenv;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Getter
public class SpeechToText {
    private final SpeechSettings settings;
    private final RecognitionConfig config;

    public SpeechToText() throws IOException {
        Dotenv dotenv = Dotenv.load();
        String googleAuthKeyPath = dotenv.get("GOOGLE_APPLICATION_CREDENTIALS");
        // Load the Google Cloud credentials from a JSON file
        assert googleAuthKeyPath != null;
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(googleAuthKeyPath));

        // Build the SpeechSettings object with the credentials
        settings = SpeechSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();

        // Builds the recognition request
        config = RecognitionConfig.newBuilder().setEncoding(RecognitionConfig.AudioEncoding.LINEAR16).setLanguageCode("en-US").setSampleRateHertz(16000).build();
    }

    public String recognize(String filename) {
        try (SpeechClient speechClient = SpeechClient.create(settings)) {
            Path path = Paths.get(filename);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speechClient.recognize(config, audio);

            List<SpeechRecognitionResult> results = response.getResultsList();
            if (!results.isEmpty()) {
                // Returns the transcribed text
                SpeechRecognitionAlternative alternative = results.get(0).getAlternativesList().get(0);
                return alternative.getTranscript();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
