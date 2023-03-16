package org.response.database;

import lombok.Data;
import lombok.RequiredArgsConstructor;
/**
 * @author Tomas Kozakas
 */

@Data
@RequiredArgsConstructor
public class Message {
    private String userMessage;
    private String chatbotMessage;

    public Message(String userMessage, String chatbotMessage) {
        this.userMessage = userMessage;
        this.chatbotMessage = chatbotMessage;
    }
}