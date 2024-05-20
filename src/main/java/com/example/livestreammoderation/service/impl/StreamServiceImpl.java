package com.example.livestreammoderation.service.impl;

import com.example.livestreammoderation.service.ModerationService;
import com.example.livestreammoderation.service.StreamService;
import com.example.livestreammoderation.service.ZegoService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class StreamServiceImpl implements StreamService {

    private boolean isStreaming = false;
    private String generatedLink = "http://example.com/stream"; // Placeholder link
    private final ModerationService moderationService;
    private final SimpMessagingTemplate messagingTemplate;
    private Timer snapshotTimer;
    private final ZegoService zegoService;

    public StreamServiceImpl(ModerationService moderationService, SimpMessagingTemplate messagingTemplate,
            ZegoService zegoService) {
        this.moderationService = moderationService;
        this.messagingTemplate = messagingTemplate;
        this.zegoService = zegoService;
    }

    @Override
    public String startStream(String moderationType) {
        if (isStreaming) {
            return "Stream is already running. Share this link with your viewers: " + generatedLink;
        }

        try {
            // Initialize the Zego SDK and start the stream
            zegoService.initializeZegoSdk();
            zegoService.startZegoStream("streamID");

            isStreaming = true;
            generatedLink = "http://example.com/stream"; // Replace with actual link

            if ("full".equalsIgnoreCase(moderationType)) {
                moderateFullStream();
            } else if ("snapshot".equalsIgnoreCase(moderationType)) {
                moderateSnapshotStream();
            }
        } catch (Exception e) {
            return "Failed to start stream: " + e.getMessage();
        }

        return "Stream started with " + moderationType + " moderation. Share this link with your viewers: "
                + generatedLink;
    }

    @Override
    public String stopStream() {
        if (!isStreaming) {
            return "No stream is currently running.";
        }

        try {
            // Terminate the stream using the Zego SDK
            zegoService.stopZegoStream();

            isStreaming = false;

            if (snapshotTimer != null) {
                snapshotTimer.cancel();
            }
        } catch (Exception e) {
            return "Failed to stop stream: " + e.getMessage();
        }

        return "Stream stopped.";
    }

    private void moderateFullStream() {
        new Thread(() -> {
            while (isStreaming) {
                try {
                    byte[] frame = zegoService.captureFrame();
                    if (frame.length == 0) {
                        continue; // Skip empty frames
                    }
                    List<String> labels = moderationService.moderateFrame(frame);
                    if (!labels.isEmpty()) {
                        notifyUser("Inappropriate content detected: " + String.join(", ", labels));
                        stopStream();
                    }
                    Thread.sleep(1000); // Adjust this interval based on your requirements
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private void moderateSnapshotStream() {
        snapshotTimer = new Timer();
        snapshotTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isStreaming) {
                    byte[] frame = zegoService.captureFrame();
                    if (frame.length == 0) {
                        return; // Skip empty frames
                    }
                    List<String> labels = moderationService.moderateFrame(frame);
                    if (!labels.isEmpty()) {
                        notifyUser("Inappropriate content detected: " + String.join(", ", labels));
                        stopStream();
                    }
                }
            }
        }, 0, 10000); // 10 seconds interval
    }

    private void notifyUser(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}
