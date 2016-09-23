package sample.domain;

/**
 *
 * This class is used to serialize Exception to a JSON representation.
 *
 * @author duplyk.a
 */
public class ExceptionJSONInfo {
	private String url;
	private String message;
	private String stackTrace;

	/**
	 *
	 * @param url
	 *            URL of the request
	 * @param ex
	 *            Exception passed
	 */
	public ExceptionJSONInfo(String url, Exception ex) {
		this.url = url;
		this.message = ex.getMessage();

		StringBuilder sb = new StringBuilder();

		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0, n = elements.length; i < n; i++) {

			sb.append(elements[i].getFileName() + ":" + elements[i].getLineNumber() + ">> "
					+ elements[i].getMethodName() + "()");
		}

		this.stackTrace = sb.toString();

	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
