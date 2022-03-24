package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.Message;
import com.erikfakin.springchatsupport.models.Notification;
import com.erikfakin.springchatsupport.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    // Used only by the support user. Gets all messages in the chatroom and marks the one sent by the client user as "seen"
    @Override
    public List<Message> findAllByChatroomId(UUID chatroomId) {
        List<Message> seenMessages = messageRepository.findAllByChatroomId(chatroomId).stream().map(message -> {
            if (message.getSender().equals("client")) {
                message.setSeen(new Date());
            }
            return message;
        }).collect(Collectors.toList());
        List<Message> saved = messageRepository.saveAll(seenMessages);

        Chatroom chatroom = chatroomService.findById(chatroomId).get();

        Notification notification = new Notification();
        notification.setType(Notification.Type.MESSAGE_SEEN);
        notification.setChatroom(chatroom);
        notification.setMessages(saved);
        template.convertAndSend("/chatroom/"+chatroomId, notification);

        return saved;
    }


    // Counts all the new messages for the support user sent by the client user.
    @Override
    public Long countNewByChatroomId(UUID chatroomId) {
        return messageRepository.countByChatroomIdAndSenderAndSeenIsNull(chatroomId, "client");
    }

    // Finds all the new messages sent by the sender and marks them as "seen".
    // If the sender is the support user it sends a new notification to the client user, else it sends the notification to the support user.
    // Once the notification is received the user gets the new messages.
    @Override
    public List<Message> findAllNewByChatroomId(String sender, UUID chatroomId) {

        List<Message> newMessages = messageRepository.findAllByChatroomIdAndSenderAndSeenIsNull(chatroomId, sender);
        System.out.println(newMessages);
        List<Message> seenMessages = newMessages.stream().peek(message -> message.setSeen(new Date())).collect(Collectors.toList());
        List<Message> saved = messageRepository.saveAll(seenMessages);

        Chatroom chatroom = chatroomService.findById(chatroomId).get();
        Notification notification = new Notification();
        notification.setType(Notification.Type.MESSAGE_SEEN);
        notification.setChatroom(chatroom);
        notification.setMessages(seenMessages);

        if (sender.equals("support")) {
            template.convertAndSend("/chatroom/notifications", notification);
        } else {
            template.convertAndSend("/chatroom/"+chatroomId, notification);
        }

        return saved;
    }

    // Sends a new message and associates it with the chatroom and the sender.
    // Sends a new notification to the client user if the sender is the support user or to the support user if the sender is the client user.
    @Override
    public Message sendMessage(UUID chatroomId, String sender, Message message) {
        Optional<Chatroom> chatroomOptional = chatroomService.findById(chatroomId);
        if (chatroomOptional.isEmpty()) {
            throw new EntityNotFoundException("Chatroom not found");
        }
        Chatroom chatroom = chatroomOptional.get();
        message.setChatroom(chatroom);
        message.setSender(sender);

        Notification notification = new Notification();
        notification.setType(Notification.Type.MESSAGE_NEW);
        notification.setChatroom(chatroom);

        Message saved = save(message);

        System.out.println(sender);

        if (sender.equals("support")) {
            template.convertAndSend("/chatroom/"+chatroomId, notification);
        } else {
            template.convertAndSend("/chatroom/notifications", notification);
        }


        return saved;
    }


}
