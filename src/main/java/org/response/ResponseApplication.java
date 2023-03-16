package org.response;

import lombok.SneakyThrows;
import org.response.database.Message;
import org.response.database.MessageDatabase;
import org.response.voice.AudioRecorder;
import org.response.voice.SpeechToText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Tomas Kozakas
 */

public class ResponseApplication {
    @SneakyThrows
    public static void main(String[] args) {
        MessageDatabase messageDatabase = new MessageDatabase();
        // Load conversation with previous messages
        List<Message> messageList = messageDatabase.getAll();
        if (messageList != null) {
            ProcessBuilder pb = new ProcessBuilder("python", "chatGPT.py", messageList.toString());
            pb.start();
        }

        AudioRecorder audioRecorder = new AudioRecorder();
        audioRecorder.openLine();

        System.out.println("Start speaking to chatGPT");
        do {
            audioRecorder.startRecording("audio.wav");

            SpeechToText speechToText = new SpeechToText();
            speechToText.recognize();
            String userMessage = speechToText.getText();

            System.out.print("Input: " + userMessage);

            if (userMessage != null) {
                if (userMessage.equals("bye") || userMessage.equals("Bye")) {
                    audioRecorder.closeLine();
                    break;
                }
                // create a ProcessBuilder for running the Python program
                ProcessBuilder pb = new ProcessBuilder("python", "chatGPT.py", userMessage);
                pb.redirectErrorStream(true); // redirect the error stream to the standard output
                // start the process
                Process process = pb.start();

                // read the output of the process
                System.out.print("Response: ");
                InputStream is = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder chatbotMessage = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    chatbotMessage.append(line);
                }
                Message message = new Message(userMessage, chatbotMessage.toString());
                messageDatabase.save(message);
            }
        } while (true);
    }
}
