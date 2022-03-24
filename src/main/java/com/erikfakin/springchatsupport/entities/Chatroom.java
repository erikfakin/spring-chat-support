package com.erikfakin.springchatsupport.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    public enum Status {
        ONLINE, OFFLINE
    }

    @ManyToOne
    private ClientUser clientUser;

    @Column(unique = true)
    private String sessionId;

}
