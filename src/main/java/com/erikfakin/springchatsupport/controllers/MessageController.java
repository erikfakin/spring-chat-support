package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.Message;
import com.erikfakin.springchatsupport.models.Greeting;
import com.erikfakin.springchatsupport.models.HelloMessage;
import com.erikfakin.springchatsupport.repositories.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@RestController
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/send")
    public void sendMessage(Message message) throws Exception {
        log.info("sadws");
        System.out.println(message);
//        Message messageToSave = new Message();
//        message.setContent(message.getContent());
//        message.setChatroom(message.getChatroom());
//        messageRepository.save(messageToSave);
        template.convertAndSend("/chatroom/1", message.getContent());

    }



}
