package org.response.openai.service;

import org.response.openai.dto.Message;
import org.response.openai.dto.RequestMessage;
import org.response.openai.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomas Kozakas
 */
@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<RequestMessage> getAllRequestMessages() {
        List<Message> messages = getAllMessages();
        List<RequestMessage> requestMessages = new ArrayList<>();

        for (Message message : messages) {
            RequestMessage requestMessage = new RequestMessage();
            requestMessage.setRole("user");
            requestMessage.setContent(message.getUserContent());
            requestMessages.add(requestMessage);

            requestMessage = new RequestMessage();
            requestMessage.setRole("assistant");
            requestMessage.setContent(message.getAssistantContent());
            requestMessages.add(requestMessage);
        }

        return requestMessages;
    }
}
