package com.erikfakin.springchatsupport.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ClientUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;
}
