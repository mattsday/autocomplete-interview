package uk.co.msday.job.autocompletemap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class FileStorageConfig {
	@Value("${autocomplete.bucket-name}")
	private String bucketName;
	@Value("${autocomplete.product-list}")
	private String productList;
	@Value("${autocomplete.promotions-list}")
	private String promotionsList;
	@Value("${autocomplete.destination-file}")
	private String destinationFile;

}
