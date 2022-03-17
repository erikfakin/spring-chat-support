package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.Message;
import com.erikfakin.springchatsupport.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/send/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, Message message) throws Exception {
        Message messageToSave = new Message();
        message.setContent(message.getContent());
        message.setChatroom(message.getChatroom());
        messageRepository.save(messageToSave);
        template.convertAndSend("/room/" + roomId, message);

    }

}
