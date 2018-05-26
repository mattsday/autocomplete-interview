package uk.co.msday.job.autocomplete.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import uk.co.msday.job.autocomplete.repo.AutocompleteRedisRepo;

@Service
@AllArgsConstructor
public class QueryService {

	private AutocompleteRedisRepo dbConfig;

	public List<String> query(String q) {
		// Map<String, List<String>> db = dbConfig.getDb();

		if ((q == null) || (q.equals(""))) {
			return new ArrayList<String>(0);
		}
		q = q.toLowerCase().replaceAll("\"", "").replaceAll("[^A-Za-z0-9 ]", "");
		String[] words = q.split(" ");

		try {
			List<String> firstWord = dbConfig.findById(words[0]).get().getProducts();
			if (firstWord == null) {
				return new ArrayList<String>(0);
			}
			List<String> proposal = new ArrayList<>(firstWord);
			// Work backwards as newer things likely to fail faster
			for (int i = (words.length - 1); i > 0; i--) {
				if (words[i].equals(""))
					continue;
				List<String> word = dbConfig.findById(words[i]).get().getProducts();
				if (word == null) {
					return new ArrayList<String>();
				}
				proposal.retainAll(word);
			}
			if (proposal.size() > 10) {
				return proposal.subList(0, 10);
			}
			return proposal;
		} catch (NoSuchElementException e) {
			return new ArrayList<String>();
		}
	}

}
