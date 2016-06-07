package sample.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import sample.domain.ExceptionJSONInfo;

@EnableWebMvc
@RestController
public class SampleRest {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionJSONInfo>  handleException(HttpServletRequest request, Exception ex) {
		ExceptionJSONInfo response = new ExceptionJSONInfo();
		response.setUrl(request.getRequestURL().toString());
		response.setMessage(ex.getMessage());

		return new ResponseEntity<ExceptionJSONInfo>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
