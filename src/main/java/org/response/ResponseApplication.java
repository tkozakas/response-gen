package org.response;

import lombok.SneakyThrows;
import org.response.openai.Chatgpt;
import org.response.openai.service.ChatgptService;
import org.response.voice.AudioRecorder;
import org.response.voice.SpeechToText;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Tomas Kozakas
 */

public class ResponseApplication {
    @SneakyThrows
    public static void main(String[] args) {
        ChatgptService chatgptService = new AnnotationConfigApplicationContext(Chatgpt.class).getBean(ChatgptService.class);
        AudioRecorder audioRecorder = new AudioRecorder();
        SpeechToText speechToText = new SpeechToText();

        System.out.println("Start speaking to chatGPT");

        audio(chatgptService, audioRecorder, speechToText);
        console(chatgptService);
    }

    private static void console(ChatgptService chatgptService) {
        Thread consoleThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String userMessage = scanner.nextLine();
                String responseMessage = chatgptService.sendMessage(userMessage);
                System.out.println("Response: " + responseMessage);
            }
        });
        consoleThread.start();
    }

    private static void audio(ChatgptService chatgptService, AudioRecorder audioRecorder, SpeechToText speechToText) {
        AtomicBoolean keepRecording = new AtomicBoolean(true);
        Thread audioThread = new Thread(() -> {
            audioRecorder.openLine();
            while (keepRecording.get()) {
                audioRecorder.startRecording("audio.wav");
                speechToText.recognize("audio.wav");
                String userMessage = speechToText.getText();
                if (userMessage != null) {
                    String responseMessage = chatgptService.sendMessage(userMessage);
                    System.out.println("Response: " + responseMessage);
                }
                audioRecorder.stopRecording();
            }
        });
        audioThread.start();
    }
}

