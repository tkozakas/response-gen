package org.response;

import lombok.SneakyThrows;
import org.response.openai.Chatgpt;
import org.response.openai.service.ChatgptService;
import org.response.openai.service.MessageService;
import org.response.voice.AudioRecorder;
import org.response.voice.SpeechToText;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Tomas Kozakas
 */

public class ResponseApplication {
    @SneakyThrows
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Chatgpt.class);
        ChatgptService chatgptService = context.getBean(ChatgptService.class);
        MessageService messageService = context.getBean(MessageService.class);

        System.out.println("Start speaking to chatGPT");

        //audio(chatgptService, messageService);
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

    @SneakyThrows
    private static void audio(ChatgptService chatgptService, MessageService messageService) {
        AudioRecorder audioRecorder = new AudioRecorder();
        SpeechToText speechToText = new SpeechToText();
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

                    //messageService.save(new Message("user", userMessage));
                    //messageService.save(new Message("assistant", responseMessage));
                }
                audioRecorder.stopRecording();
            }
        });
        audioThread.start();
    }
}

