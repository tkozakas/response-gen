package org.response;

import lombok.SneakyThrows;
import org.response.database.Message;
import org.response.database.MessageDatabase;
import org.response.voice.AudioRecorder;
import org.response.voice.SpeechToText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Tomas Kozakas
 */

public class ResponseApplication {
    @SneakyThrows
    public static void main(String[] args) {
        ProcessBuilder pb;

        AudioRecorder audioRecorder = new AudioRecorder();
        SpeechToText speechToText = new SpeechToText();
        MessageDatabase messageDatabase = new MessageDatabase();
        List<Message> messageList = messageDatabase.getAll();


        // Load conversation with previous messages
        if (messageList != null) {
            StringBuilder lastMessages = new StringBuilder();
            messageList.forEach(m -> lastMessages
                    .append("user: ")
                    .append(m.getUserMessage())
                    .append("\n")
                    .append("chatGPT: ")
                    .append(m.getChatbotMessage())
                    .append('\n'));
            System.out.println("Last messages: \n" + lastMessages);
            pb = new ProcessBuilder("python", "chatGPT.py",
                    "<<user:>> is me and <<chatGPT:>> is you. Now you answer and don't write <<user:>> or <<chatGPT>> at the start. This is out last messages: " + lastMessages);
            outputProcess(pb);
        }

        audioRecorder.openLine();
        System.out.println("Start speaking to chatGPT");
        while (true) {
            audioRecorder.startRecording("audio.wav");

            speechToText.recognize();
            String userMessage = speechToText.getText();

            if (userMessage != null) {
                System.out.println("Input: " + userMessage);

                // create a ProcessBuilder for running the Python program
                pb = new ProcessBuilder("python", "chatGPT.py", userMessage);
                pb.redirectErrorStream(true); // redirect the error stream to the standard output
                String botMessage = outputProcess(pb);
                messageDatabase.save(new Message(userMessage, botMessage));
            }
        }
    }

    private static String outputProcess(ProcessBuilder pb) throws IOException {
        // read the output of the process
        System.out.print("Response: ");

        Process process = pb.start();
        InputStream is = process.getInputStream();
        String line;
        StringBuilder message = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            message.append(line);
        }
        return message.toString();
    }
}
