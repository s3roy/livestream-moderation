package com.example.livestreammoderation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class LivestreamModerationApplication {

	static {
		String os = System.getProperty("os.name").toLowerCase();
		String libraryPath = System.getProperty("user.dir") + "/src/main/resources/native";
		if (os.contains("win")) {
			System.load(libraryPath + "/zego_express_engine.dll");
		} else if (os.contains("nux") || os.contains("nix") || os.contains("mac")) {
			System.load(libraryPath + "/libZegoExpressEngine.so");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(LivestreamModerationApplication.class, args);
	}
}
