package com.erikfakin.springchatsupport.controllers;

import com.erikfakin.springchatsupport.entities.Notification;
import com.erikfakin.springchatsupport.services.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationServiceImpl notificationService;


    @GetMapping("/new")
    public List<Notification> getAllNewNotifications() {
        return notificationService.getAllNewNotifications();
    }

    @GetMapping("/seen")
    public List<Notification> getAllSeenNotifications() {
        return notificationService.getAllSeenNotifications();
    }


}
