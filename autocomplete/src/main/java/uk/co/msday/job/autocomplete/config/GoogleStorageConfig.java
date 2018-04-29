package uk.co.msday.job.autocomplete.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class GoogleStorageConfig {

	@Bean
	public Storage googleStorage() {
		return StorageOptions.getDefaultInstance().getService();
	}

}
