package com.erikfakin.springchatsupport.services;


import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.repositories.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatroomServiceImpl implements ChatroomService {

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Override
    public Chatroom save(Chatroom chatroom) {
        return chatroomRepository.save(chatroom);
    }

    @Override
    public List<Chatroom> findAll() {
        return chatroomRepository.findAll();
    }

    @Override
    public List<Chatroom> findAllByStatus(Chatroom.Status status) {
        return chatroomRepository.findAllByStatus(status);
    }
}
