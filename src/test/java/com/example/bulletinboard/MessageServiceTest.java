
package com.example.bulletinboard;

import com.example.bulletinboard.Message;
import com.example.bulletinboard.Channel;
import com.example.bulletinboard.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Unit test: verifies MessageService.addMessage() saves and returns a message
     */
    @Test
    void testAddMessage_Success() {
        Message message = new Message();
        message.setContent("Hello World");

        when(messageRepository.save(message)).thenReturn(message);

        Message savedMessage = messageService.addMessage(message);

        assertNotNull(savedMessage);
        assertEquals("Hello World", savedMessage.getContent());
        verify(messageRepository, times(1)).save(message);
    }

    /**
     * Unit test: verifies MessageService.updateMessage() updates content when message exists
     */
    @Test
    void testUpdateMessage_MessageExists() {
        Long messageId = 1L;
        Message existingMessage = new Message();
        existingMessage.setId(messageId);
        existingMessage.setContent("Old Content");

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(existingMessage));
        when(messageRepository.save(existingMessage)).thenReturn(existingMessage);

        Message updatedMessage = messageService.updateMessage(messageId, "New Content");

        assertNotNull(updatedMessage);
        assertEquals("New Content", updatedMessage.getContent());
        verify(messageRepository, times(1)).findById(messageId);
        verify(messageRepository, times(1)).save(existingMessage);
    }

    /**
     * Unit test: verifies MessageService.updateMessage() returns null when message does not exist
     */
    @Test
    void testUpdateMessage_MessageNotFound() {
        Long messageId = 1L;

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        Message updatedMessage = messageService.updateMessage(messageId, "New Content");

        assertNull(updatedMessage);
        verify(messageRepository, times(1)).findById(messageId);
        verify(messageRepository, times(0)).save(any());
    }

    /**
     * Unit test: verifies MessageService.deleteMessage() deletes message by id
     */
    @Test
    void testDeleteMessage_Success() {
        Long messageId = 1L;

        doNothing().when(messageRepository).deleteById(messageId);

        messageService.deleteMessage(messageId);

        verify(messageRepository, times(1)).deleteById(messageId);
    }
}
