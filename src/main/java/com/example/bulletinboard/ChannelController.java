package com.example.bulletinboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/channels") // Base URL: http://localhost:8080/channels
public class ChannelController {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private MessageRepository messageRepository;

    // 1. GET - Retrieve all channels
    @GetMapping("/")
    public ResponseEntity<List<Channel>> getAllChannels() {
        return ResponseEntity.ok(channelRepository.findAll());
    }

    // 2. POST - Create a new channel
    @PostMapping("/")
    public ResponseEntity<Channel> createChannel(@RequestBody Channel channel) {
        Channel savedChannel = channelRepository.save(channel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedChannel);
    }

    // 3. DELETE - Delete a channel by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable Long id) {
        if (!channelRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        channelRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //  4. PUT - Add a message to a channel
    @PutMapping("/{channelId}")
    public ResponseEntity<Message> addMessageToChannel(@PathVariable Long channelId, @RequestBody Message message) {
        Optional<Channel> optionalChannel = channelRepository.findById(channelId);
        if (optionalChannel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Channel channel = optionalChannel.get();
        message.setChannel(channel);
        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.ok(savedMessage);
    }

    //  5. GET - Retrieve all messages in a channel
    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> getChannelMessages(@PathVariable Long channelId) {
        Optional<Channel> optionalChannel = channelRepository.findById(channelId);
        return optionalChannel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //  6. PUT - Update a message in a channel (VG Requirement)
    @PutMapping("/messages/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long messageId, @RequestBody Message updatedMessage) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Message existingMessage = optionalMessage.get();
        existingMessage.setContent(updatedMessage.getContent());
        Message savedMessage = messageRepository.save(existingMessage);
        return ResponseEntity.ok(savedMessage);
    }

    //  7. DELETE - Delete a message from a channel (VG Requirement)
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        if (!messageRepository.existsById(messageId)) {
            return ResponseEntity.notFound().build();
        }
        messageRepository.deleteById(messageId);
        return ResponseEntity.noContent().build();
    }
}
