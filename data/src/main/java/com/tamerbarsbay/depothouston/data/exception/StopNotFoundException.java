package com.tamerbarsbay.depothouston.data.exception;

/**
 * Created by Tamer on 7/23/2015.
 */
public class StopNotFoundException extends Exception {

    public StopNotFoundException() {
        super();
    }

    public StopNotFoundException(final String message) {
        super(message);
    }

    public StopNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public StopNotFoundException(final Throwable cause) {
        super(cause);
    }
}
