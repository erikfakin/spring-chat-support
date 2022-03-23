package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.Message;
import com.erikfakin.springchatsupport.entities.Notification;
import com.erikfakin.springchatsupport.repositories.ChatroomRepository;
import com.erikfakin.springchatsupport.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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
        return messageRepository.findAllByChatroomId(chatroomId);
    }

    @Override
    public List<Message> findAllByChatroomIdAndStatus(UUID chatroomId, Message.Status status) {
        List<Message> newMessages = messageRepository.findAllByChatroomIdAndStatus(chatroomId, status);
        List<Message> updatedMessages = newMessages.stream().peek(message -> message.setStatus(Message.Status.SEEN)).collect(Collectors.toList());
        messageRepository.saveAll(updatedMessages);

        Chatroom chatroom = chatroomService.findBySessionId(chatroomId.toString());

        Notification notification = new Notification();
        notification.setStatus(Notification.Status.NEW);
        notification.setType(Notification.Type.MESSAGE_SEEN);
        notification.setChatroom(chatroom);
        notificationService.save(notification);

        template.convertAndSend("/chatroom/"+chatroomId, notification);
        template.convertAndSend("/chatroom/notifications", notification);


        return newMessages;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Long countByChatroomIdAndStatus(UUID chatroomId, Message.Status status) {
        return messageRepository.countByChatroomIdAndStatus(chatroomId, status);
    }
}
