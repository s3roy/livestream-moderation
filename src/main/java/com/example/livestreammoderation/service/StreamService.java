package com.example.livestreammoderation.service;

public interface StreamService {
    String startStream(String moderationType);

    String stopStream();
}
