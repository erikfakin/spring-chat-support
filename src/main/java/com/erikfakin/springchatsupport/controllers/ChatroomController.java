package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.ClientUser;
import com.erikfakin.springchatsupport.repositories.ChatroomRepository;
import com.erikfakin.springchatsupport.repositories.ClientUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/room")
public class ChatroomController {

    @Autowired
    private ChatroomRepository chatRoomRepository;
    @Autowired
    private SimpUserRegistry simpUserRegistry;
    @Autowired
    private ClientUserRepository clientUserRepository;




    @GetMapping()
    public Chatroom getNewChatRoom(@RequestBody ClientUser clientUser) {

        ClientUser user =clientUserRepository.findByEmail(clientUser.getEmail());

        Chatroom chatroom = new Chatroom();
        chatroom.setClientUser(user);
        log.info(chatroom.toString());
        return chatRoomRepository.save(chatroom);
    }

    @GetMapping("/all")
    public List<Chatroom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }
}
