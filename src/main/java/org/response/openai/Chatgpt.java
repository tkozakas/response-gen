package org.response.openai;

import lombok.SneakyThrows;
import org.response.openai.dto.Message;
import org.response.openai.service.ChatgptService;
import org.response.openai.service.MessageService;
import org.response.openai.voice.AudioRecorder;
import org.response.openai.voice.SpeechToText;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Tomas Kozakas
 */
@SpringBootApplication
public class Chatgpt implements CommandLineRunner {
    private final ChatgptService chatgptService;
    private final MessageService messageService;

    public Chatgpt(ChatgptService chatgptService, MessageService messageService) {
        this.chatgptService = chatgptService;
        this.messageService = messageService;
    }

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(Chatgpt.class, args);
    }

    @Override
    public void run(String... args) {
        console(chatgptService, messageService);
        audio(chatgptService, messageService);
    }

    private static void console(ChatgptService chatgptService, MessageService messageService) {
        Thread consoleThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String userMessage = scanner.nextLine();
                String responseMessage = chatgptService.sendMessage(userMessage);
                System.out.println("Response: " + responseMessage);
                messageService.save(new Message(userMessage, responseMessage));
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
            while (keepRecording.get()) {
                audioRecorder.openLine();
                audioRecorder.startRecording("audio.wav");
                String userMessage = speechToText.recognize("audio.wav");
                if (userMessage != null) {
                    System.out.println(userMessage);
                    String responseMessage = chatgptService.sendMessage(userMessage);
                    System.out.println("Response: " + responseMessage);
                    messageService.save(new Message(userMessage, responseMessage));
                }
            }
        });
        audioThread.start();
    }
}


