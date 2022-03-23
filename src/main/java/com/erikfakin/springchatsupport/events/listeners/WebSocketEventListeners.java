package com.erikfakin.springchatsupport.events.listeners;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.Notification;
import com.erikfakin.springchatsupport.services.ChatroomService;
import com.erikfakin.springchatsupport.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Configuration
public class WebSocketEventListeners {

    @Autowired
    private ChatroomService chatroomService;

    @Autowired
    private NotificationService notificationService;

    @EventListener
    public void onSocketConnected(SessionConnectedEvent event) {
//        System.out.println(event);
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[Connected] " + sha.getSessionId());
    }


    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[Disonnected] " + sha.getSessionId());

        if (sha.getSessionId().contains("client")) {
            Chatroom chatroom = chatroomService.findBySessionId(sha.getSessionId());
            chatroom.setStatus(Chatroom.Status.OFFLINE);
            chatroomService.save(chatroom);
            List<Chatroom> chatrooms = chatroomService.findAll();
            System.out.println(chatrooms);

            notificationService.sendNotification(Notification.Type.CHATROOM_OFFLINE, chatroom);
        }





    }

    @EventListener
    public void onSessionSubscribe(SessionSubscribeEvent event){
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String destination = sha.getDestination();
        if (Objects.nonNull(destination) && !destination.contains("notifications")) {
            String sessionId = sha.getSessionId();
            UUID roomId = UUID.fromString(destination.substring(destination.lastIndexOf("/") + 1).trim());
            Chatroom chatroom = chatroomService.findById(roomId).get();

            if (Objects.isNull(chatroom.getSessionId()) && sessionId.contains("client")){
                chatroom.setSessionId(sessionId);
                chatroomService.save(chatroom);
            }
            notificationService.sendNotification(Notification.Type.CHATROOM_ONLINE, chatroom);
        }



    }

    @EventListener
    public void onSessionUnsubscribe(SessionUnsubscribeEvent event){

        System.out.println("Disconnected!!!");

        System.out.println(event.getMessage().getHeaders().get("simpDestination"));

//        String destination = event.getMessage().getHeaders().get("simpDestination").toString();
//        UUID roomId = UUID.fromString(destination.substring(destination.lastIndexOf("/") + 1).trim());
//        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
//        Chatroom chatroom = chatroomRepository.findById(roomId).get();
//        if (Objects.isNull(chatroom.getSessionId()){
//            chatroom.setSessionId(sessionId);
//            chatroomRepository.save(chatroom);
//        }
//
//        List<Chatroom> chatrooms = chatroomRepository.findAll();
//
//        System.out.println(chatrooms);

//        System.out.println(sessionId);
    }
}
