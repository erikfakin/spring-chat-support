package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.Notification;
import com.erikfakin.springchatsupport.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private NotificationRepository notificationRepository;


    public List<Notification> getAllNewNotifications() {
        List<Notification> newNotifications = notificationRepository.findAllByStatus(Notification.Status.NEW);
        List<Notification> updatedNotifications = newNotifications.stream().peek(notification -> notification.setStatus(Notification.Status.SEEN)).collect(Collectors.toList());
        notificationRepository.saveAll(updatedNotifications);

        return newNotifications;
    }

    public List<Notification> getAllSeenNotifications() {
        return notificationRepository.findAllByStatus(Notification.Status.SEEN);
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
}
