package com.example.livestreammoderation.service;

import java.util.List;

public interface ModerationService {
    List<String> moderateFrame(byte[] imageBytes);
}
