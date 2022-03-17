package com.erikfakin.springchatsupport.repositories;

import com.erikfakin.springchatsupport.entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
