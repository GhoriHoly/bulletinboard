package com.example.bulletinboard;


import com.example.bulletinboard.MessageService;
import com.example.bulletinboard.Channel;
import com.example.bulletinboard.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class) // Load only MessageController
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService; // Only mock service, not repository

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Component Test: Verifies GET /messages/channel/{channelId} returns a list of messages
     */
    @Test
    void testGetMessagesByChannel() throws Exception {
        Long channelId = 1L;

        Channel channel = new Channel();
        channel.setId(channelId);

        Message message1 = new Message();
        message1.setId(1L);
        message1.setContent("First message");
        message1.setChannel(channel);

        Message message2 = new Message();
        message2.setId(2L);
        message2.setContent("Second message");
        message2.setChannel(channel);

        List<Message> messages = Arrays.asList(message1, message2);

        when(messageService.getMessagesByChannel(channelId)).thenReturn(messages);

        mockMvc.perform(get("/messages/channel/{channelId}", channelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("First message"))
                .andExpect(jsonPath("$[1].content").value("Second message"));

        verify(messageService, times(1)).getMessagesByChannel(channelId);
    }

    /**
     * Component Test: Verifies POST /messages creates a message and returns 201 Created
     */
    @Test
    void testAddMessage() throws Exception {
        Message message = new Message();
        message.setId(1L);
        message.setContent("New Message");

        when(messageService.addMessage(any(Message.class))).thenReturn(message);

        mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("New Message"));

        verify(messageService, times(1)).addMessage(any(Message.class));
    }
}
