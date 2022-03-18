package com.erikfakin.springchatsupport.config;

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

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

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
        System.out.println(event);
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[Connected] " + sha.getSessionId());
    }


    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        System.out.println(event);
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("[Disonnected] " + sha.getSessionId());
    }

    @EventListener
    public void onSessionSubscribe(SessionSubscribeEvent event){
        System.out.println(event);
    }

    @EventListener
    public void onSessionUnsubscribe(SessionUnsubscribeEvent event){
        System.out.println(event);
    }
}
