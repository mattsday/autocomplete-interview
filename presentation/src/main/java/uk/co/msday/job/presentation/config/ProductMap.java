package uk.co.msday.job.presentation.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;

import lombok.Getter;
import lombok.NonNull;
import uk.co.msday.job.presentation.config.model.Product;

@Service
public class ProductMap {

	@NonNull
	@Getter
	private HashMap<String, Product> db;

	@NonNull
	private Storage storage;

	@NonNull
	private FileStorageConfig config;

	public ProductMap(Storage storage, FileStorageConfig config) throws IOException {
		this.storage = storage;
		this.config = config;
		db = new HashMap<>();
		update();
	}

	public void update() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<Product> productList = mapper.readValue(storage.readAllBytes(config.getBucketName(), config.getFileName()),
				new TypeReference<List<Product>>() {
				});
		for (Product p : productList) {
			String key = p.getName();
			if (key != null) {
				db.put(key, p);
			}
		}
	}
}
