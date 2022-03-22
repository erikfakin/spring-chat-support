package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.Notification;

import java.util.List;
import java.util.stream.Collectors;

public interface NotificationService {
    public List<Notification> getAllNewNotifications();

    public List<Notification> getAllSeenNotifications();

    public Notification save(Notification notification);
}
