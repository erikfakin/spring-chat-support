package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.Chatroom;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ChatroomService {
    Chatroom save(Chatroom chatroom);

    List<Chatroom> findAll();

    List<Chatroom> findAllByStatus(Chatroom.Status status);

    Chatroom findBySessionId(String chatroomId);

    Optional<Chatroom> findById(UUID roomId);
}
