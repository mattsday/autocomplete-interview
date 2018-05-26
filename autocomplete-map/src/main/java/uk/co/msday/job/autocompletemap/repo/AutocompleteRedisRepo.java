package uk.co.msday.job.autocompletemap.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.co.msday.job.autocompletemap.model.AutocompleteEntry;

@Repository
public interface AutocompleteRedisRepo extends CrudRepository<AutocompleteEntry, String> {
}
