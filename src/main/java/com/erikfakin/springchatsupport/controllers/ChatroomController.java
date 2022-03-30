package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.ClientUser;
import com.erikfakin.springchatsupport.services.ChatroomService;
import com.erikfakin.springchatsupport.services.ClientUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/room")
public class ChatroomController {

    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private ClientUserService clientUserService;
    @Autowired
    private SimpUserRegistry simpUserRegistry;
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * The client user sends a post request and gets a new chatroom to listen for notifications.
     * The new chatroom is marked as "online" and a notification is sent to the support user.
     * If the user already used the chatroom with the email specified, it updates the name and loads that user.
     * If the email specified is new, we create a new client user.
     */
    @PostMapping()
    public Chatroom getNewChatRoom(@RequestBody ClientUser clientUser, @RequestHeader Map<String, String> header) {
        if (Objects.isNull(clientUser)) {
            throw new NullPointerException("Client user can't be null.");
        }
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
        Chatroom savedChatroom =  chatroomService.save(chatroom);

        return savedChatroom;
    }

    /**
     * Gets a chatroom by its ID.
     */
    @GetMapping("/{id}")
    public Chatroom findById(@PathVariable("id") UUID id) {
        if (Objects.isNull(id)) {
            throw new NullPointerException("Chatroom id can't be null.");
        }
        return chatroomService.findById(id).get();
    }

    /**
     * Gets all chatrooms by their statuses.
     */
    @GetMapping("/status/{status}")
    public List<Chatroom> findAllByStatus(@PathVariable("status") String status) {
        if (Objects.isNull(status)) {
            throw new NullPointerException("Chatroom status can't be null");
        }
        Chatroom.Status chatroomStatus = Chatroom.Status.valueOf(status.toUpperCase());
        return chatroomService.findAllByStatus(chatroomStatus);
    }

    @GetMapping("/all")
    public List<Chatroom> getAllChatRooms() {
        return chatroomService.findAll();
    }
}