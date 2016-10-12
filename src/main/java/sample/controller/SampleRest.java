package sample.controller;

import java.time.LocalDateTime;

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

	/**
	 * Endpoint to test LocalDateTime output, taht should be formatted as ISO
	 * date.
	 *
	 * @return wrapped entity
	 */
	@GetMapping("now")
	public ResponseEntity<LocalDateTime> getNow() {
		return new ResponseEntity<>(LocalDateTime.now(), HttpStatus.OK);
	}
}
