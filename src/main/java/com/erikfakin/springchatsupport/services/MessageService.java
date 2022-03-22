package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message save(Message messageToSave);

    List<Message> findAllByChatroomId(UUID chatroomId);

    List<Message> findAllByChatroomIdAndStatus(UUID chatroomId, Message.Status aNew);

    List<Message> findAll();
}
