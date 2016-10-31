package com.movie.exceptions;

/**
 * Exception class to handle the case when the html parser didn't find what we wanted.
 * @author Kévin
 */
public class HtmlParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4173551403063977671L;

	public HtmlParserException() {
	}

	public HtmlParserException(String arg0) {
		super(arg0);
	}

	public HtmlParserException(Throwable arg0) {
		super(arg0);
	}

	public HtmlParserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public HtmlParserException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
