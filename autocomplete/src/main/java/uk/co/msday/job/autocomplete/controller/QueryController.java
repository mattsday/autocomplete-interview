package uk.co.msday.job.autocomplete.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import uk.co.msday.job.autocomplete.services.QueryService;

@RestController
@AllArgsConstructor
public class QueryController {

	private QueryService service;

	@GetMapping("/get")
	@CrossOrigin(origins = "*")
	public List<String> query(String q) {
		return service.query(q);
	}
}
