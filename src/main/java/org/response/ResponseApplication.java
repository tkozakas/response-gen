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
import java.util.Scanner;

/**
 * @author Tomas Kozakas
 */

public class ResponseApplication {
    private static MessageDatabase messageDatabase;

    @SneakyThrows
    public static void main(String[] args) {
        AudioRecorder audioRecorder = new AudioRecorder();
        SpeechToText speechToText = new SpeechToText();
        messageDatabase = new MessageDatabase();
        List<Message> messageList = messageDatabase.getAll();


        System.out.println("Start speaking to chatGPT");

//        AtomicBoolean keepRecording = new AtomicBoolean(true);
//        Thread audioThread = new Thread(() -> {
//            audioRecorder.openLine();
//            while (keepRecording.get()) {
//                audioRecorder.startRecording("audio.wav");
//                speechToText.recognize("audio.wav");
//                String userMessage = speechToText.getText();
//                if (userMessage != null) {
//                    System.out.println(userMessage);
//                    response(userMessage);
//                }
//                audioRecorder.stopRecording();
//            }
//        });
//        audioThread.start();

        // Create a separate thread for reading input from console
        Thread consoleThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String userMessage = scanner.nextLine();
                response(userMessage);
            }
        });
        consoleThread.start();

    }

    @SneakyThrows
    private static void response(String userMessage) {
        ProcessBuilder pb = new ProcessBuilder("python", "chatGPT.py", userMessage);
        pb.redirectErrorStream(true);
        String botMessage = outputProcess(pb);

        messageDatabase.save(new Message(userMessage, botMessage));
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

