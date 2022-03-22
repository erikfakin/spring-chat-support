package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.Message;
import com.erikfakin.springchatsupport.entities.Notification;
import com.erikfakin.springchatsupport.repositories.ChatroomRepository;
import com.erikfakin.springchatsupport.services.MessageService;
import com.erikfakin.springchatsupport.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private MessageService messageService
            ;
    @Autowired
    private ChatroomRepository chatRoomRepository;
    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/send/{chatroomId}")
    public void sendMessage(@DestinationVariable String chatroomId, Message message) throws Exception {

        Message messageToSave = new Message();
        messageToSave.setContent(message.getContent());
        Chatroom chatroom = chatRoomRepository.findById(UUID.fromString(chatroomId)).get();
        messageToSave.setChatroom(chatroom);
        messageService.save(messageToSave);

        Notification notification = new Notification();
        notification.setType(Notification.Type.MESSAGE_NEW);
        notification.setStatus(Notification.Status.NEW);
        notification.setChatroom(chatroom);
        notificationService.save(notification);


        template.convertAndSend("/chatroom/"+chatroomId, notification);
        template.convertAndSend("/chatroom/notifications", notification);

    }

    @GetMapping("/messages/{chatroomId}")
    public List<Message> getAllMessagesByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageService.findAllByChatroomId(chatroomId);
    }

    @GetMapping("/messages/new/{chatroomId}")
    public List<Message> findAllNewMessagesByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageService.findAllByChatroomIdAndStatus(chatroomId, Message.Status.NEW);
    }

    @GetMapping("/messages")
    public List<Message> getALlMessages() {
        return messageService.findAll();
    }




}
