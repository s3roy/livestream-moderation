package com.example.livestreammoderation.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    String uploadFile(MultipartFile file);

    List<String> listFiles();
}
