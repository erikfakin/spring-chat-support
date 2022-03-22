package com.erikfakin.springchatsupport.config;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.repositories.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/chatroom");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // with sockjs
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
        // without sockjs
        //registry.addEndpoint("/ws-message").setAllowedOriginPatterns("*");
    }

    @EventListener
    public void onSocketConnected(SessionConnectedEvent event) {
//        System.out.println(event);
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[Connected] " + sha.getSessionId());
    }


    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        Chatroom chatroom = chatroomRepository.findBySessionId(event.getSessionId());
        chatroom.setStatus(Chatroom.Status.OFFLINE);
        chatroomRepository.save(chatroom);
        List<Chatroom> chatrooms = chatroomRepository.findAll();
        System.out.println(chatrooms);
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[Disonnected] " + sha.getSessionId());
    }

    @EventListener
    public void onSessionSubscribe(SessionSubscribeEvent event){
        String destination = event.getMessage().getHeaders().get("simpDestination").toString();
        UUID roomId = UUID.fromString(destination.substring(destination.lastIndexOf("/") + 1).trim());
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        Chatroom chatroom = chatroomRepository.findById(roomId).get();
        if (Objects.isNull(chatroom.getSessionId()) && sessionId.contains("client")){
            chatroom.setSessionId(sessionId);
            chatroomRepository.save(chatroom);
        }

        List<Chatroom> chatrooms = chatroomRepository.findAll();
        System.out.println(chatrooms);

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
