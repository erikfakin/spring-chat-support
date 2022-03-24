package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Long countNewByChatroomId(UUID chatroomId);

    List<Message> findAllByChatroomId(UUID chatroomId);

    List<Message> findAllNewByChatroomId(String sender, UUID chatroomId);

    Message save(Message messageToSave);

    Message sendMessage(UUID chatroomId, String sender, Message message);
}
