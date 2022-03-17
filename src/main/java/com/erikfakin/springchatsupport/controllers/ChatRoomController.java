package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.ChatRoom;
import com.erikfakin.springchatsupport.repositories.ChatRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/room")
public class ChatRoomController {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @GetMapping()
    public ChatRoom getNewChatRoom() {
        ChatRoom chatRoom = new ChatRoom();
        log.info(chatRoom.toString());
        return chatRoomRepository.save(chatRoom);
    }

    @GetMapping("/all")
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }
}
