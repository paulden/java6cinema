package com.path_to_cinema;

/**
 * Exception class to handle the case when Google Maps API doesn't return any routes.
 * @author Renaud
 *
 */
public class NoPathException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoPathException() {
		// TODO Auto-generated constructor stub
	}

	public NoPathException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NoPathException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NoPathException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NoPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
