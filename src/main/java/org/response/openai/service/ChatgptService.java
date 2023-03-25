package org.response.openai.service;


import lombok.SneakyThrows;
import org.codehaus.jackson.map.ObjectMapper;
import org.response.openai.dto.ChatRequest;
import org.response.openai.dto.RequestMessage;
import org.response.openai.properties.ChatgptProperties;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * @author Tomas Kozakas
 */
@Service
public class ChatgptService {
    private final ChatgptProperties chatgptProperties = new ChatgptProperties();

    @SneakyThrows
    public String sendMessage(String input) {
        ChatRequest request = ChatRequest.builder()
                .model(chatgptProperties.getApiModel())
                .messages(List.of(RequestMessage.builder()
                        .role("user")
                        .content(input)
                        .build()
                ))
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(chatgptProperties.getApiUrl()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + chatgptProperties.getApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        String messageContent = null;
        if (httpResponse.statusCode() == 200) {
            JsonReader reader = Json.createReader(new StringReader(httpResponse.body()));
            try {
                JsonObject responseJson = reader.readObject();
                JsonObject choice = responseJson.getJsonArray("choices").getJsonObject(0);
                JsonObject message = choice.getJsonObject("message");
                messageContent = message.getString("content");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return messageContent;

    }
}
