package org.response.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    private Double temperature;

    @JsonProperty("top_p")
    private Double topP;

}
