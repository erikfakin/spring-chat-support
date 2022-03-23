package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.Message;
import com.erikfakin.springchatsupport.entities.Notification;
import com.erikfakin.springchatsupport.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findAllByChatroomId(UUID chatroomId) {
        List<Message> seenMessages = messageRepository.findAllByChatroomId(chatroomId).stream().map(message -> {
            if (message.getSender().equals("client")) {
                System.out.println("seen");
                message.setSeen(new Date());
                System.out.println(message);

            }
            return message;
        }).collect(Collectors.toList());


        List<Message> saved = messageRepository.saveAll(seenMessages);
        System.out.println(saved);
        return saved;
    }


    @Override
    public List<Message> findAll() {
        List<Message> seenMessages = messageRepository.findAll().stream().map(message -> {
            if (message.getSender() == "client") {
                System.out.println(message);
                message.setSeen(new Date());

            }

            return message;
        }).collect(Collectors.toList());

        List<Message> saved = messageRepository.saveAll(seenMessages);
        System.out.println(saved);
        return saved;
    }

    @Override
    public Long countNewByChatroomId(UUID chatroomId) {
        return messageRepository.countByChatroomIdAndSenderAndSeenIsNull(chatroomId, "client");
    }

    @Override
    public List<Message> findAllNewByChatroomId(String sender, UUID chatroomId) {

        List<Message> newMessages = messageRepository.findAllByChatroomIdAndSenderAndSeenIsNull(chatroomId, sender);
        List<Message> seenMessages = newMessages.stream().peek(message -> message.setSeen(new Date())).collect(Collectors.toList());
        List<Message> saved = messageRepository.saveAll(seenMessages);

        Chatroom chatroom = chatroomService.findById(chatroomId).get();
        Notification notification = new Notification();
        notification.setType(Notification.Type.MESSAGE_SEEN);
        notification.setChatroom(chatroom);

        if (sender == "support") {
            template.convertAndSend("/chatroom/notifications", notification);
        } else {
            template.convertAndSend("/chatroom/"+chatroomId, notification);
        }

        return saved;
    }




}
