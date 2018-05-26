package uk.co.msday.job.autocomplete.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.co.msday.job.autocomplete.model.AutocompleteEntry;

@Repository
public interface AutocompleteRedisRepo extends CrudRepository<AutocompleteEntry, String> {

	// public List<>

}
