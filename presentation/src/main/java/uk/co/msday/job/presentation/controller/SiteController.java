package uk.co.msday.job.presentation.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.co.msday.job.presentation.config.ProductMap;
import uk.co.msday.job.presentation.config.model.Product;

@Controller
@RequiredArgsConstructor
public class SiteController {

	@NonNull
	private ProductMap db;

	@Value("${autocomplete.service}")
	private String autocompleteService;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("autocomplete", autocompleteService);
		return "index";
	}

	@GetMapping("/product")
	public String product(Model model, @RequestParam(name = "q", required = true) String product) {
		if (!db.getDb().containsKey(product)) {
			return "error";
		}
		Product p = db.getDb().get(product);
		model.addAttribute("product", p);
		model.addAttribute("autocomplete", autocompleteService);
		return "product";
	}

}
