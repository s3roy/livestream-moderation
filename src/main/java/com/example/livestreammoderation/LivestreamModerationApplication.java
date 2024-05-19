package com.example.livestreammoderation;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class LivestreamModerationApplication {

	public static void main(String[] args) {
		// Load environment variables from .env file
		Dotenv dotenv = Dotenv.load();

		System.setProperty("aws.accessKeyId", dotenv.get("AWS_ACCESS_KEY_ID"));
		System.setProperty("aws.secretKey", dotenv.get("AWS_SECRET_ACCESS_KEY"));
		System.setProperty("aws.region", dotenv.get("AWS_REGION"));
		System.setProperty("aws.s3.bucket.name", dotenv.get("S3_BUCKET_NAME"));

		SpringApplication.run(LivestreamModerationApplication.class, args);
	}
}
