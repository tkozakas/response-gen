package org.voice.recognition;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SpeechToText {
    public static void printToFile(String outputText, String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(outputText);
            writer.close();
            System.out.println("Text saved to file at " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        try (SpeechClient speechClient = SpeechClient.create()) {
            // The path to the audio file to transcribe
            String filename = "audio.wav";
            Path path = Paths.get(filename);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            // Builds the recognition request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("en-US")
                    .setSampleRateHertz(16000)
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            // Prints the transcribed text
            for (SpeechRecognitionResult result : results) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                printToFile(alternative.getTranscript(), "speech.txt");
            }
        }
    }
}
