package com.erikfakin.springchatsupport.services;


import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.repositories.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatroomService {

    @Autowired
    private ChatroomRepository chatroomRepository;

    public Chatroom save(Chatroom chatroom) {
        return chatroomRepository.save(chatroom);
    }

    public List<Chatroom> findAll() {
        return chatroomRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    public List<Chatroom> findAllByStatus(Chatroom.Status status) {
        return chatroomRepository.findAllByStatus(status);
    }

    public Chatroom findBySessionId(String chatroomId) {
        return chatroomRepository.findBySessionId(chatroomId.toString());
    }

    public Optional<Chatroom> findById(UUID roomId) {
        return chatroomRepository.findById(roomId);
    }
}