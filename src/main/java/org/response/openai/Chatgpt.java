package org.response.openai;

import org.response.openai.service.ChatgptService;
import org.response.openai.service.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tomas Kozakas
 */
@SpringBootApplication
public class Chatgpt {
    private final ChatgptService chatgptService;
    private final MessageService messageService;

    public Chatgpt(ChatgptService chatgptService, MessageService messageService) {
        this.chatgptService = chatgptService;
        this.messageService = messageService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Chatgpt.class, args);
    }
}


