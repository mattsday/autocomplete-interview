package uk.co.msday.job.autocomplete.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;

import lombok.Getter;
import lombok.NonNull;

@Configuration
public class AutocompleteMap {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@NonNull
	private Storage storage;

	@NonNull
	private FileStorageConfig config;

	@Getter
	private Map<String, List<String>> db;

	public AutocompleteMap(Storage storage, FileStorageConfig config) throws IOException {
		this.storage = storage;
		this.config = config;
		update();
	}

	public void update() throws IOException {
		log.info("Updating autocomplete map");
		ObjectMapper mapper = new ObjectMapper();
		db = mapper.readValue(storage.readAllBytes(config.getBucketName(), config.getFileName()),
				new TypeReference<HashMap<String, List<String>>>() {
				});
		log.info("Map updated - " + db.size() + " keys");
	}

}
