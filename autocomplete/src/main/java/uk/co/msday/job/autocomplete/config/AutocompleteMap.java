package uk.co.msday.job.autocomplete.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;

import lombok.Getter;
import lombok.NonNull;

@Configuration
public class AutocompleteMap {

	@NonNull
	private Storage storage;

	private FileStorageConfig config;

	@Getter
	private Map<String, List<String>> db;

	public AutocompleteMap(Storage storage, FileStorageConfig config) throws IOException {
		this.storage = storage;
		this.config = config;
		update();
	}

	public void update() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		db = mapper.readValue(storage.readAllBytes(config.getBucketName(), config.getFileName()),
				new TypeReference<Map<String, List<String>>>() {
				});
	}

}
