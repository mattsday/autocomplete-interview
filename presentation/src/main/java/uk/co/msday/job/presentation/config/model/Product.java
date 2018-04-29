package uk.co.msday.job.presentation.config.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
	private String name;
	private String price;
	private String description;
	private String type;
	private String manufacturer;
	private String image;
}
