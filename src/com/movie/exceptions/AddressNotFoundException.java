package com.movie.exceptions;

/**
 * Exception class to handle the case when Google Maps API doesn't find the address.
 * @author Renaud
 *
 */
public class AddressNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public AddressNotFoundException() {
    }

    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException(Throwable cause) {
        super(cause);
    }

    public AddressNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
