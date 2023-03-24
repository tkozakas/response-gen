package org.response.openai.properties;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

/**
 * @author Tomas Kozakas
 */

@Getter
public class ChatgptProperties {
    private final String apiKey;
    private final String apiUrl;
    private final String apiModel;
    private final Double temperature;
    private final Integer maxTokens;

    public ChatgptProperties() {
        Dotenv dotenv = Dotenv.load();
        apiKey = dotenv.get("API_KEY");
        apiUrl = "https://api.openai.com/v1/chat/completions";
        apiModel = "gpt-3.5-turbo";
        temperature = 0.0;
        maxTokens = 100;
    }

}
