package com.erikfakin.springchatsupport.repositories;

import com.erikfakin.springchatsupport.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByStatus(Notification.Status status);
}
