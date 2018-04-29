package uk.co.msday.job.autocomplete.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class FileStorageConfig {
	@Value("${autocomplete.bucket-name}")
	private String bucketName;
	@Value("${autocomplete.file-name}")
	private String fileName;

}
