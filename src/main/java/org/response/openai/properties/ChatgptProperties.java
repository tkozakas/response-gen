package org.response.openai.properties;

import lombok.Getter;

/**
 * @author Tomas Kozakas
 */

@Getter
public class ChatgptProperties {
    private final String apiKey = System.getenv("API_KEY");
    private final String apiUrl = "https://api.openai.com/v1/chat/completions";
    private final String apiModel = "gpt-3.5-turbo";
    private final Double temperature = 0.0;
    private final Integer maxTokens = 100;
}
