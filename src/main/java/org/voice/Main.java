package org.voice;

import javax.sound.sampled.LineUnavailableException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException, LineUnavailableException {
        AudioRecorder audioRecorder = new AudioRecorder();
        audioRecorder.openLine();

        System.out.println("Start speaking to chatGPT");
        while (true) {
            audioRecorder.startRecording("audio.wav");

            SpeechToText speechToText = new SpeechToText();
            speechToText.recognize();
            String text = speechToText.getText();

            if (text != null) {
                // create a ProcessBuilder for running the Python program
                ProcessBuilder pb = new ProcessBuilder("python", "chatGPT.py", text);
                pb.redirectErrorStream(true); // redirect the error stream to the standard output
                // start the process
                Process process = pb.start();

                // read the output of the process
                InputStream is = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }
        //audioRecorder.closeLine();

    }
}
