package sample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Sample controller class to check application is alive.
 *
 * @author Andrii Duplyk
 *
 */
@EnableWebMvc
@RestController
public class SampleRest {

	/**
	 * Live test endpoint
	 *
	 * @return true as JSON
	 */
	@GetMapping("test")
	public ResponseEntity<Boolean> testController() {
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}
}
