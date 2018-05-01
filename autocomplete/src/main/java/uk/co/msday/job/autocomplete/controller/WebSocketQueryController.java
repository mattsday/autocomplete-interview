package uk.co.msday.job.autocomplete.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import uk.co.msday.job.autocomplete.services.QueryService;

@RestController
@AllArgsConstructor
public class WebSocketQueryController {

	private QueryService service;

	@MessageMapping("/ws-get")
	@SendTo("/topic/ws-autocomplete")
	public List<String> query(String q) {
		return service.query(q);
	}
}
