/**
 *
 */
package sample.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import sample.domain.ExceptionJSONInfo;
import sample.exceptions.BusinessLogicException;

/**
 * @author duplyk.a
 *
 *         Global handler for BusinessLogicException -s and server ones.
 */
@EnableWebMvc
@ControllerAdvice(basePackages = { "sample.controller" })
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BusinessLogicException.class)
	public ResponseEntity<ExceptionJSONInfo> handleBusinessLogicException(HttpServletRequest request, Exception ex) {
		ExceptionJSONInfo response = getExceptionJSONInfoFromException(ex, request);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionJSONInfo> handleOtherExceptions(Exception ex, HttpServletRequest request) {
		ExceptionJSONInfo response = getExceptionJSONInfoFromException(ex, request);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ExceptionJSONInfo getExceptionJSONInfoFromException(Exception ex, HttpServletRequest request) {
		ExceptionJSONInfo response = new ExceptionJSONInfo(request.getRequestURL().toString(), ex);
		return response;
	}

}
