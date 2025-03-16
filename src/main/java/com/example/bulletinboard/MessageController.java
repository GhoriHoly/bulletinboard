package com.example.bulletinboard;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/")
    public Message addMessage(@RequestBody Message message) {
        return messageService.addMessage(message);
    }

    @GetMapping("/{channelId}")
    public List<Message> getMessagesByChannel(@PathVariable Long channelId) {
        return messageService.getMessagesByChannel(channelId);
    }

    @PutMapping("/{id}")
    public Message updateMessage(@PathVariable Long id, @RequestBody String content) {
        return messageService.updateMessage(id, content);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
    }
}
