package com.erikfakin.springchatsupport.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
public class Message {

    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    private Long id;
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;

    private String sender;
    private Date seen;

    @ManyToOne
    private Chatroom chatroom;


    @PrePersist
    private void onCreate() {
        timestamp = new Date();
    }


}
