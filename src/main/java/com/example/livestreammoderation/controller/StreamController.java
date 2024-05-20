package com.example.livestreammoderation.controller;

import com.example.livestreammoderation.service.StreamService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stream")
public class StreamController {

    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @PostMapping("/start")
    public String startStream(@RequestParam String moderationType) {
        return streamService.startStream(moderationType);
    }

    @PostMapping("/stop")
    public String stopStream() {
        return streamService.stopStream();
    }
}
