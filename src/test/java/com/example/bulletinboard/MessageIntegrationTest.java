package com.example.bulletinboard;


import com.example.bulletinboard.Channel;
import com.example.bulletinboard.Message;
import com.example.bulletinboard.ChannelRepository;
import com.example.bulletinboard.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.*;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageIntegrationTest {

    @com.example.bulletinboard.LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    private Channel savedChannel;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        messageRepository.deleteAll();
        channelRepository.deleteAll();

        // Create and save a Channel for testing
        Channel channel = new Channel();
        channel.setName("Test Channel");
        savedChannel = channelRepository.save(channel);
    }

    /**
     * Integration Test: Tests the full flow of creating and retrieving a Message
     */
    @Test
    void testCreateAndRetrieveMessage() {
        // Prepare message to send
        Message message = new Message();
        message.setContent("Integration Test Message");
        message.setChannel(savedChannel);

        // Send POST request to create Message
        ResponseEntity<Message> postResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/messages",
                message,
                Message.class
        );

        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());
        assertNotNull(postResponse.getBody().getId());
        assertEquals("Integration Test Message", postResponse.getBody().getContent());

        // Now test GET request to fetch messages by channel
        ResponseEntity<Message[]> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/messages/channel/" + savedChannel.getId(),
                Message[].class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(1, getResponse.getBody().length);
        assertEquals("Integration Test Message", getResponse.getBody()[0].getContent());
    }
}
