package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.ClientUser;
import com.erikfakin.springchatsupport.repositories.ChatroomRepository;
import com.erikfakin.springchatsupport.repositories.ClientUserRepository;
import com.erikfakin.springchatsupport.services.ChatroomService;
import com.erikfakin.springchatsupport.services.ClientUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/room")
public class ChatroomController {

    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private SimpUserRegistry simpUserRegistry;
    @Autowired
    private ClientUserService clientUserService;




    @PostMapping()
    public Chatroom getNewChatRoom(@RequestBody ClientUser clientUser, @RequestHeader Map<String, String> header) {


        ClientUser user = clientUserService.findByEmail(clientUser.getEmail());
        if (Objects.isNull(user)) {
            user = new ClientUser();
            user.setEmail(clientUser.getEmail());
            user.setName(clientUser.getName());
            clientUserService.save(user);
        } else if(user.getName() != clientUser.getName()){
            user.setName(clientUser.getName());
            clientUserService.save(user);
        }

        Chatroom chatroom = new Chatroom();
        chatroom.setClientUser(user);
        chatroom.setStatus(Chatroom.Status.ONLINE);
        return chatroomService.save(chatroom);
    }

    @GetMapping("/{status}")
    public List<Chatroom> findAllByStatus(@PathVariable("status") Chatroom.Status status) {
        return chatroomService.findAllByStatus(status);
    }

    @GetMapping("/all")
    public List<Chatroom> getAllChatRooms() {
        return chatroomService.findAll();
    }
}
