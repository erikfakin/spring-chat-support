package com.erikfakin.springchatsupport.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Data
public class ChatRoom {

    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;



}
