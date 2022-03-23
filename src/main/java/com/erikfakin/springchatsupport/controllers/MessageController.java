package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.Message;
import com.erikfakin.springchatsupport.entities.Notification;
import com.erikfakin.springchatsupport.repositories.ChatroomRepository;
import com.erikfakin.springchatsupport.services.MessageService;
import com.erikfakin.springchatsupport.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ChatroomRepository chatRoomRepository;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/messages/{chatroomId}")
    public Message sendMessage(@PathVariable("chatroomId") UUID chatroomId, @RequestParam("sender") String sender, @RequestBody Message message) {
        Optional<Chatroom> chatroomOptional = chatRoomRepository.findById(chatroomId);
        if (chatroomOptional.isEmpty()) {
            throw new EntityNotFoundException("Chatroom not found");
        }
        Chatroom chatroom = chatroomOptional.get();
        message.setChatroom(chatroom);
        message.setSender(sender);

        Notification notification = new Notification();
        notification.setType(Notification.Type.MESSAGE_NEW);
        notification.setChatroom(chatroom);

        if (sender == "support") {
            template.convertAndSend("/chatroom/"+chatroomId, notification);
        } else {
            template.convertAndSend("/chatroom/notifications", notification);
        }

        Message saved = messageService.save(message);
        return saved;
    }

    @GetMapping("/messages/{chatroomId}")
    public List<Message> getAllMessagesByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageService.findAllByChatroomId(chatroomId);
    }

    @GetMapping("/messages/new/client/{chatroomId}")
    public List<Message> findAllNewMessagesForClientByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageService.findAllNewByChatroomId("support", chatroomId);
    }

    @GetMapping("/messages/new/support/{chatroomId}")
    public List<Message> findAllNewMessagesForSupportByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageService.findAllNewByChatroomId("client", chatroomId);
    }

    @GetMapping("/messages/new/support/{chatroomId}/count")
    public Long countAllNewMessagesByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageService.countNewByChatroomId(chatroomId);

    }

    @GetMapping("/messages")
    public List<Message> getALlMessages() {
        return messageService.findAll();
    }




}
