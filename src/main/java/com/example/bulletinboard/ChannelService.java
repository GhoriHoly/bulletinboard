package com.example.bulletinboard;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public Channel createChannel(Channel channel) {
        return channelRepository.save(channel);
    }

    public void deleteChannel(Long id) {
        channelRepository.deleteById(id);
    }

    public Channel getChannelById(Long id) {
        return channelRepository.findById(id).orElse(null);
    }
}
