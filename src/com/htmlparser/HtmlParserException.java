package com.htmlparser;

/**
 * Exception si le parser html n'a pas trouvé ce qu'il recherchait.
 * @author Kévin
 *
 */
public class HtmlParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4173551403063977671L;

	public HtmlParserException() {
		// TODO Auto-generated constructor stub
	}

	public HtmlParserException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public HtmlParserException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public HtmlParserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public HtmlParserException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
