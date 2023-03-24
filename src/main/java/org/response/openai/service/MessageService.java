package org.response.openai.service;

import org.response.openai.dto.Message;
import org.response.openai.repository.MessageRepository;
import org.springframework.stereotype.Service;

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
}
