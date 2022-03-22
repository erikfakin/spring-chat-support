package com.erikfakin.springchatsupport.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Notification {

    @Id
    @GeneratedValue
    private Long id;


    private Type type;
    private Status status;

    @ManyToOne
    private Chatroom chatroom;

    public enum Status {
        NEW, SEEN
    }

    public enum Type {
        MESSAGE_NEW, MESSAGE_SEEN, CHATROOM_ONLINE, CHATROOM_OFFLINE
    }

}
