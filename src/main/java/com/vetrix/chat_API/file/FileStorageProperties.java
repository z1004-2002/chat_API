package com.vetrix.chat_API.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "data")
public class FileStorageProperties {
	private String productDir;
}