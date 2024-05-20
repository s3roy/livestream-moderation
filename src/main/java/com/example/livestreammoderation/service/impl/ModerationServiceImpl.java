package com.example.livestreammoderation.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.example.livestreammoderation.service.ModerationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModerationServiceImpl implements ModerationService {

        private final AmazonRekognition rekognitionClient;

        public ModerationServiceImpl(@Value("${aws.accessKeyId}") String accessKeyId,
                        @Value("${aws.secretKey}") String secretKey,
                        @Value("${aws.region}") String region) {
                this.rekognitionClient = AmazonRekognitionClientBuilder.standard()
                                .withRegion(Regions.fromName(region))
                                .withCredentials(new AWSStaticCredentialsProvider(
                                                new BasicAWSCredentials(accessKeyId, secretKey)))
                                .build();
        }

        @Override
        public List<String> moderateFrame(byte[] imageBytes) {
                if (imageBytes.length == 0) {
                        throw new IllegalArgumentException("Image data is empty");
                }

                ByteBuffer imageBytesBuffer = ByteBuffer.wrap(imageBytes);
                Image image = new Image().withBytes(imageBytesBuffer);

                DetectModerationLabelsRequest request = new DetectModerationLabelsRequest()
                                .withImage(image)
                                .withMinConfidence(75F);

                DetectModerationLabelsResult result = rekognitionClient.detectModerationLabels(request);
                return result.getModerationLabels().stream()
                                .map(ModerationLabel::getName)
                                .collect(Collectors.toList());
        }
}
