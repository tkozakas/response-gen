package org.response.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;
/**
 * @author Tomas Kozakas
 */
@Entity
@Table(name = "messages")

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "created_at")
    private LocalDateTime timestamp;
    @Column(name = "user_message")
    private String userMessage;
    @Column(name = "chatbot_message")
    private String chatbotMessage;

    public Message(LocalDateTime timestamp, String userMessage, String chatbotMessage) {
        this.timestamp = timestamp;
        this.userMessage = userMessage;
        this.chatbotMessage = chatbotMessage;
    }
}