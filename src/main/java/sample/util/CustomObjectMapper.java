package sample.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class CustomObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = -3672344428949816140L;

	public CustomObjectMapper() {
		// SerializationFeature for changing how JSON is written

		// to enable standard indentation ("pretty-printing"):
		configure(SerializationFeature.INDENT_OUTPUT, true);
		// to allow serialization of "empty" POJOs (no properties to serialize)
		// (without this setting, an exception is thrown in those cases)
		configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// to write java.util.Date, Calendar as number (timestamp):
		configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		configure(SerializationFeature.WRAP_EXCEPTIONS, true);

		// DeserializationFeature for changing how JSON is read as POJOs:

		// to prevent exception when encountering unknown property:
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// to allow coercion of JSON empty String ("") to null Object value:
		configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

		configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		// This cleans LocalDateTime representation.
		registerModule(new JavaTimeModule());

	}

}
