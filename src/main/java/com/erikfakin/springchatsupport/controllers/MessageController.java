package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.Message;
import com.erikfakin.springchatsupport.repositories.ChatroomRepository;
import com.erikfakin.springchatsupport.repositories.MessageRepository;
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
    private MessageRepository messageRepository;
    @Autowired
    private ChatroomRepository chatRoomRepository;

    @MessageMapping("/send/{chatroomId}")
    public void sendMessage(@DestinationVariable String chatroomId, Message message) throws Exception {

        Message messageToSave = new Message();
        messageToSave.setContent(message.getContent());
        Chatroom chatroom = chatRoomRepository.findById(UUID.fromString(chatroomId)).get();
        messageToSave.setChatroom(chatroom);
        messageRepository.save(messageToSave);

        template.convertAndSend("/chatroom/"+chatroomId, message.getContent());

    }

    @GetMapping("/messages/{chatroomId}")
    public List<Message> getAllMessagesByChatroomId(@PathVariable("chatroomId") UUID chatroomId) {
        return messageRepository.findAllByChatroomId(chatroomId);
    }

    @GetMapping("/messages")
    public List<Message> getALlMessages() {
        return messageRepository.findAll();
    }




}
