package com.erikfakin.springchatsupport.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class ChatRoom {

    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    private Long id;



}
