package org.response.openai.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Tomas Kozakas
 */
@Data
@AllArgsConstructor
@Builder
public class ChatRequest {
    private String model;

    private List<Message> messages;
}
