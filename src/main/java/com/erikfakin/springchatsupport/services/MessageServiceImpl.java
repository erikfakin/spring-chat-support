package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.Message;
import com.erikfakin.springchatsupport.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findAllByChatroomId(UUID chatroomId) {
        return messageRepository.findAllByChatroomId(chatroomId);
    }

    @Override
    public List<Message> findAllByChatroomIdAndStatus(UUID chatroomId, Message.Status status) {
        List<Message> newMessages = messageRepository.findAllByChatroomIdAndStatus(chatroomId, status);
        List<Message> updatedMessages = newMessages.stream().peek(message -> message.setStatus(Message.Status.SEEN)).collect(Collectors.toList());
        messageRepository.saveAll(updatedMessages);
        return newMessages;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
