package uk.co.msday.job.autocompletemap.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@RedisHash("Autocomplete")
@Data
public class AutocompleteEntry implements Serializable {

	@Id
	private String id;
	private List<String> products;

}
