package com.erikfakin.springchatsupport.events.listeners;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.models.Notification;
import com.erikfakin.springchatsupport.services.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;


import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Configuration
public class WebSocketEventListeners {

    @Autowired
    private ChatroomService chatroomService;

    @Autowired
    private SimpMessagingTemplate template;

    // Associates the session ID of the client with the chatroom.
    // If the destination contains "notifications" we don't want to affect the chatrooms (the support user only listens to notifications globally
    // We want to associate the client session with the chatroom, so we can mark it as offline once the client disconnects.
    @EventListener
    public void onSessionSubscribe(SessionSubscribeEvent event){
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String destination = sha.getDestination();
        if (Objects.nonNull(destination) && !destination.contains("notifications")) {
            String sessionId = sha.getSessionId();
            UUID roomId = UUID.fromString(destination.substring(destination.lastIndexOf("/") + 1).trim());
            Optional<Chatroom> chatroomOptional = chatroomService.findById(roomId);
            if (chatroomOptional.isEmpty()) throw new EntityNotFoundException("Chatroom not found.");
            Chatroom chatroom = chatroomOptional.get();

            if (Objects.isNull(chatroom.getSessionId()) && sessionId.contains("client")){
                chatroom.setSessionId(sessionId);
                chatroomService.save(chatroom);
            }
            Notification notification = new Notification();
            notification.setType(Notification.Type.CHATROOM_ONLINE);
            notification.setChatroom(chatroom);
            template.convertAndSend("/chatroom/notifications", notification);
        }
    }

    // Listens for the client to unsubscribe then finds the chatroom associated with the client session and marks it offline.
    @EventListener
    public void onSessionUnsubscribe(SessionUnsubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        if (sha.getSessionId().contains("client")) {
            Chatroom chatroom = chatroomService.findBySessionId(sha.getSessionId());
            chatroom.setStatus(Chatroom.Status.OFFLINE);
            chatroomService.save(chatroom);

            Notification notification = new Notification();
            notification.setType(Notification.Type.CHATROOM_OFFLINE);
            notification.setChatroom(chatroom);
            template.convertAndSend("/chatroom/notifications", notification);
        }
    }

    // Listens for the client to disconnect then finds the chatroom associated with the client session and marks it offline.
    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        if (sha.getSessionId().contains("client")) {
            Chatroom chatroom = chatroomService.findBySessionId(sha.getSessionId());
            chatroom.setStatus(Chatroom.Status.OFFLINE);
            chatroomService.save(chatroom);

            Notification notification = new Notification();
            notification.setType(Notification.Type.CHATROOM_OFFLINE);
            notification.setChatroom(chatroom);
            template.convertAndSend("/chatroom/notifications", notification);
        }
    }






}
