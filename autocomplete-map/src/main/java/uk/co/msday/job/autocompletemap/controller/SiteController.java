package uk.co.msday.job.autocompletemap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import uk.co.msday.job.autocompletemap.services.GenerateMapService;

@Controller
// @EnableOAuth2Sso
@AllArgsConstructor
public class SiteController {
	private GenerateMapService mapService;

	@GetMapping("/update")
	public String update() {
		mapService.updateMap();
		return "OK";
	}

}
