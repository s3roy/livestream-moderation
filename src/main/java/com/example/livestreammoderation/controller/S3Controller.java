package com.example.livestreammoderation.controller;

import com.example.livestreammoderation.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam MultipartFile file) {
        return s3Service.uploadFile(file);
    }

    @GetMapping("/list")
    public List<String> listFiles() {
        return s3Service.listFiles();
    }
}
