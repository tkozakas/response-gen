package org.response.openai;

import lombok.SneakyThrows;
import org.response.openai.dto.Message;
import org.response.openai.dto.RequestMessage;
import org.response.openai.service.ChatgptService;
import org.response.openai.service.MessageService;
import org.response.openai.voice.AudioRecorder;
import org.response.openai.voice.SpeechToText;
import org.response.openai.voice.TextToSpeech;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
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
        List<RequestMessage> messageList = messageService.getAllRequestMessages();
        chatgptService.sendMessage(messageList);

        console(chatgptService, messageService, messageList);
        audio(chatgptService, messageService, messageList);
    }

    private static void console(ChatgptService chatgptService, MessageService messageService, List<RequestMessage> messageList) {
        Thread consoleThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String userMessage = scanner.nextLine();
                response(chatgptService, messageService, messageList, userMessage);
            }
        });
        consoleThread.start();
    }

    @SneakyThrows
    private static void audio(ChatgptService chatgptService, MessageService messageService, List<RequestMessage> messageList) {
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
                    response(chatgptService, messageService, messageList, userMessage);
                }
            }
        });
        audioThread.start();
    }

    private static void response(ChatgptService chatgptService, MessageService messageService, List<RequestMessage> messageList, String userMessage) {
        TextToSpeech textToSpeech = new TextToSpeech();
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setRole("user");
        requestMessage.setContent(userMessage);
        messageList.add(requestMessage);

        String responseMessage = chatgptService.sendMessage(messageList);

        requestMessage = new RequestMessage();
        requestMessage.setRole("assistant");
        requestMessage.setContent(responseMessage);
        messageList.add(requestMessage);

        System.out.println("Response: " + responseMessage);
        textToSpeech.say(responseMessage);
        messageService.save(new Message(userMessage, responseMessage));
    }
}


