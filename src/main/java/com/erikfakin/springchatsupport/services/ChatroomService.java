package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.Chatroom;

import java.util.List;

public interface ChatroomService {
    Chatroom save(Chatroom chatroom);

    List<Chatroom> findAll();

    List<Chatroom> findAllByStatus(Chatroom.Status status);
}
