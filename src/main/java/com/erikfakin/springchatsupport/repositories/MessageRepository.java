package com.erikfakin.springchatsupport.repositories;

import com.erikfakin.springchatsupport.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatroomId(UUID chatroomId);

    List<Message> findAllByChatroomIdAndStatus(UUID chatroomId, Message.Status status);

    Long countByChatroomIdAndStatus(UUID chatroomId, Message.Status status);
}
