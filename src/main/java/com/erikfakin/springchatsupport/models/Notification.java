package com.erikfakin.springchatsupport.models;

import com.erikfakin.springchatsupport.entities.Chatroom;
import com.erikfakin.springchatsupport.entities.Message;
import lombok.Data;

import java.util.List;

@Data
public class Notification {

    private Type type;
    private List<Message> messages;
    private Chatroom chatroom;

    public enum Type {
        MESSAGE_NEW, MESSAGE_SEEN, CHATROOM_ONLINE, CHATROOM_OFFLINE
    }
}