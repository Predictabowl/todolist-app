package it.aldinucci.todoapp.learning;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@ExtendWith(SpringExtension.class)
class LearningTest {

//	@Value("${server.port}")
//	private int port;
	
	
	@Test
	void test() throws URISyntaxException {
//		System.out.println(port);
//		String uriString = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//		System.out.println(uriString);
		String uriString = UriComponentsBuilder
			.fromUriString("http://localhost:8080/register/verify?carlo=mario")
			.replaceQuery("")
			.queryParam("token", "string-code")
			.build().toUriString();
		
		System.out.println(uriString);
		
		URI uri = new URI(uriString);
		System.out.println(uri.getHost());
		System.out.println(uri.getPort());
		System.out.println(uri.getPath());
		System.out.println(uri.getRawQuery());
	}
	

}
