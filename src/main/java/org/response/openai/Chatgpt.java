package org.response.openai;

import org.response.openai.service.ChatgptService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tomas Kozakas
 */
@SpringBootApplication
public class Chatgpt {
    private final ChatgptService chatgptService;

    public Chatgpt(ChatgptService chatgptService) {
        this.chatgptService = chatgptService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Chatgpt.class, args);
    }
}


