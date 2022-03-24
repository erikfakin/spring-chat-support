package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.Message;
import com.erikfakin.springchatsupport.repositories.ChatroomRepository;
import com.erikfakin.springchatsupport.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Sends a message
    @PostMapping("/messages/{chatroomId}")
    public Message sendMessage(@PathVariable("chatroomId") UUID chatroomId, @RequestParam("sender") String sender, @RequestBody Message message) {
        return messageService.sendMessage(chatroomId, sender, message);
    }

    // Gets all messages in a chatroom
    @GetMapping("/messages/{chatroomId}")
    public List<Message> getAllMessagesByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageService.findAllByChatroomId(chatroomId);
    }

    // Gets all new messages for the client user, sent by support
    @GetMapping("/messages/new/client/{chatroomId}")
    public List<Message> findAllNewMessagesForClientByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageService.findAllNewByChatroomId("support", chatroomId);
    }

    // Gets all new messages for the support user, sent by client
    @GetMapping("/messages/new/support/{chatroomId}")
    public List<Message> findAllNewMessagesForSupportByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageService.findAllNewByChatroomId("client", chatroomId);
    }

    // Counts all new messages for the support user, sent by the client
    @GetMapping("/messages/new/support/{chatroomId}/count")
    public Long countAllNewMessagesByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageService.countNewByChatroomId(chatroomId);

    }

}
