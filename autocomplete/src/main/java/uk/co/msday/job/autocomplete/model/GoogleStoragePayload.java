package uk.co.msday.job.autocomplete.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
// Only care about name and bucket for this
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleStoragePayload {
	String name;
	String bucket;
}
