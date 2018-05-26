package uk.co.msday.job.autocompletemap.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;

import lombok.AllArgsConstructor;
import uk.co.msday.job.autocompletemap.config.FileStorageConfig;
import uk.co.msday.job.autocompletemap.model.AutocompleteEntry;
import uk.co.msday.job.autocompletemap.model.Product;
import uk.co.msday.job.autocompletemap.repo.AutocompleteRedisRepo;

@AllArgsConstructor
@Service
public class GenerateMapService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private Storage storage;
	private FileStorageConfig config;
	private AutocompleteRedisRepo repo;

	private List<Product> getProductList() {
		ObjectMapper mapper = new ObjectMapper();
		List<Product> products;
		try {
			products = mapper.readValue(storage.readAllBytes(config.getBucketName(), config.getProductList()),
					new TypeReference<List<Product>>() {
					});
		} catch (Exception e) {
			throw new RuntimeException("Cannot read file from storage: " + e);
		}

		return products;
	}

	private List<String> getPromotions() {
		ObjectMapper mapper = new ObjectMapper();
		List<String> products;
		try {
			products = mapper.readValue(storage.readAllBytes(config.getBucketName(), config.getPromotionsList()),
					new TypeReference<List<String>>() {
					});
		} catch (Exception e) {
			throw new RuntimeException("Cannot read file from storage " + e);
		}
		return products;
	}

	public SortedMap<String, List<String>> updateMap() {
		log.info("Updating prefixes");
		SortedMap<String, List<String>> pointerMap = new TreeMap<>();

		List<Product> products = getProductList();
		log.info("Downloaded products, building initial list");

		for (Product product : products) {
			if (product.getName() == null) {
				continue;
			}
			String searchName = product.getName().toLowerCase().replaceAll("\"", "").replaceAll("[^A-Za-z0-9 ]", "");
			// Split by word
			String[] words = searchName.split(" ");
			for (String word : words) {
				for (int i = 1; i <= word.length(); i++) {
					String searchTerm = word.substring(0, i);
					List<String> matchList = pointerMap.get(searchTerm);
					if (matchList == null) {
						matchList = new ArrayList<String>(100);
						pointerMap.put(searchTerm, matchList);
					}
					matchList = pointerMap.get(searchTerm);
					matchList.add(product.getName());
				}
			}
		}

		log.info("Sorting precedence for first-match");

		// Give precedence to items that start with the search key
		for (String key : pointerMap.keySet()) {
			List<String> data = pointerMap.get(key);
			int swap = 0;
			for (int i = 0; i < data.size(); i++) {
				String item = data.get(i);
				String itemQuery = item.toLowerCase().replaceAll(" ", "").replaceAll("[^A-Za-z0-9 ]", "").substring(0,
						key.length());
				if (itemQuery.equals(key)) {
					Collections.swap(data, i, swap++);
				}
			}
		}

		log.info("Adding promotions");

		// Now add promotions
		List<String> promotions = getPromotions();

		for (String product : promotions) {
			String searchName = product.toLowerCase().replaceAll("\"", "").replaceAll("[^A-Za-z0-9 ]", "");
			String[] words = searchName.split(" ");
			for (String word : words) {
				for (int i = 1; i <= word.length(); i++) {
					String searchTerm = word.substring(0, i);
					List<String> matchList = pointerMap.get(searchTerm);
					if (matchList == null) {
						matchList = new ArrayList<String>(100);
						pointerMap.put(searchTerm, matchList);
					}
					matchList = pointerMap.get(searchTerm);
					matchList.add(0, product);
				}
			}
		}

		log.info("Prefix list updated - " + pointerMap.size() + " keys");

		log.info("Adding to Redis");

		for (String prefix : pointerMap.keySet()) {
			AutocompleteEntry e = new AutocompleteEntry();
			e.setId(prefix);
			e.setProducts(pointerMap.get(prefix));
			repo.save(e);
		}

		/*
		 * BlobId blobId = BlobId.of(config.getBucketName(),
		 * config.getDestinationFile()); BlobInfo blobInfo =
		 * BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
		 * ObjectMapper mapper = new ObjectMapper(); try { storage.create(blobInfo,
		 * mapper.writeValueAsBytes(pointerMap)); } catch (JsonProcessingException e) {
		 * throw new RuntimeException("Can't write to Google Cloud: " + e); }
		 * log.info("Done uploading prefixes");
		 */

		return pointerMap;
	}

}
