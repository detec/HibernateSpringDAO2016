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
 * Global handler for BusinessLogicException instances and server ones.
 *
 * @author Andrii Duplyk
 *
 */
@EnableWebMvc
@ControllerAdvice(basePackages = { "sample.controller" })
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Method catches {@link BusinessLogicException}
	 *
	 * @param request
	 *            HttpServletRequest request
	 * @param exception
	 *            {@link Exception} caught
	 * @return ResponseEntity<ExceptionJSONInfo> with constructed JSON entity.
	 */
	@ExceptionHandler(BusinessLogicException.class)
	public ResponseEntity<ExceptionJSONInfo> handleBusinessLogicException(HttpServletRequest request,
			Exception exception) {
		ExceptionJSONInfo response = getExceptionJSONInfoFromException(exception, request);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	/**
	 * Catches other technical exceptions
	 *
	 * @param exception
	 *            {@link Exception} caught
	 * @param request
	 *            HttpServletRequest request
	 * @return ResponseEntity<ExceptionJSONInfo> with constructed JSON entity.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionJSONInfo> handleOtherExceptions(Exception ex, HttpServletRequest request) {
		ExceptionJSONInfo response = getExceptionJSONInfoFromException(ex, request);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ExceptionJSONInfo getExceptionJSONInfoFromException(Exception ex, HttpServletRequest request) {
		return new ExceptionJSONInfo(request.getRequestURL().toString(), ex);
	}

}
