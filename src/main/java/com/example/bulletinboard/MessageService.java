package com.example.bulletinboard;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChannel(Long channelId) {
        return messageRepository.findByChannelId(channelId);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public Message updateMessage(Long id, String content) {
        Message message = messageRepository.findById(id).orElse(null);
        if (message != null) {
            message.setContent(content);
            return messageRepository.save(message);
        }
        return null;
    }
}
