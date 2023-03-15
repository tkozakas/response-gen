package org.response.database;

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

    public void save(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getAll() {
        return messageRepository.findAll();
    }
}
