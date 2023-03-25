package org.response.openai.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tomas Kozakas
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_content", nullable = false)
    private String userContent;

    @Column(name = "assistant_content", nullable = false)
    private String assistantContent;


    public Message(String userContent, String assistantContent) {
        this.userContent = userContent;
        this.assistantContent = assistantContent;
    }
}
