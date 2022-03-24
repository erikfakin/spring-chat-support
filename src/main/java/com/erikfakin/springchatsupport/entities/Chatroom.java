package com.erikfakin.springchatsupport.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class Chatroom {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    private Status status;



    @ManyToOne
    private ClientUser clientUser;

    @Column(unique = true)
    private String sessionId;

    private Date timestamp;


    public enum Status {
        ONLINE, OFFLINE
    }

    @PrePersist
    private void onCreate () {
        timestamp = new Date();
    }

}
