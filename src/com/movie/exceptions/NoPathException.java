package com.movie.exceptions;

/**
 * Exception class to handle the case when Google Maps API doesn't return any routes.
 * @author Renaud
 *
 */
public class NoPathException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoPathException() {
	}

	public NoPathException(String message) {
		super(message);
	}

	public NoPathException(Throwable cause) {
		super(cause);
	}

	public NoPathException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
