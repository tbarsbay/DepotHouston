package com.tamerbarsbay.depothouston.domain.exception;

/**
 * Created by Tamer on 7/22/2015.
 */
public class DefaultErrorBundle implements ErrorBundle {

    private final Exception exception;

    public DefaultErrorBundle(Exception exception) {
        this.exception = exception;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public String getErrorMessage() {
        String message = "";
        if (this.exception != null) {
            this.exception.getMessage();
        }
        return message;
    }

}
