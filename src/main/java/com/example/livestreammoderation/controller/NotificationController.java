// src/main/java/com/example/livestreammoderation/controller/NotificationController.java
package com.example.livestreammoderation.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public String notify(String message) {
        return message;
    }
}
