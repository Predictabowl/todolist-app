package it.aldinucci.todoapp.webcommons.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/webcommon/controller")
public class MockController {

	@GetMapping
	public String getToBeStubbed() {
		return null;
	}
}
