package uk.co.msday.job.autocomplete.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import uk.co.msday.job.autocomplete.config.AutocompleteMap;

@Service
@AllArgsConstructor
public class QueryService {

	private AutocompleteMap dbConfig;

	public List<String> query(String q) {
		Map<String, List<String>> db = dbConfig.getDb();

		if ((q == null) || (q.equals(""))) {
			return new ArrayList<String>(0);
		}
		q = q.toLowerCase().replaceAll("\"", "").replaceAll("[^A-Za-z0-9 ]", "");
		String[] words = q.split(" ");
		List<String> firstWord = db.get(words[0]);
		if (firstWord == null) {
			return new ArrayList<String>(0);
		}
		List<String> proposal = new ArrayList<>(firstWord);
		// Work backwards as newer things likely to fail faster
		for (int i = (words.length - 1); i > 0; i--) {
			if (words[i].equals(""))
				continue;
			List<String> word = db.get(words[i]);
			if (word == null) {
				return new ArrayList<String>();
			}
			proposal.retainAll(word);
		}
		if (proposal.size() > 10) {
			return proposal.subList(0, 10);
		}
		return proposal;
	}

}
