package com.vetrix.chat_API;

import com.vetrix.chat_API.file.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class ChatApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChatApiApplication.class, args);
	}
}
