package sample.exceptions;

/**
 *
 * Common class for decorating custom exceptions in service layer interface
 *
 */
public class BusinessLogicException extends Exception {

	private static final long serialVersionUID = 7184590489625096514L;

	/**
	 *
	 * @param message
	 *            String to be passes to super class constructor.
	 */
	public BusinessLogicException(String message) {
		super(message);
	}

}
