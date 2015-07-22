package com.tamerbarsbay.depothouston.domain.exception;

/**
 * Created by Tamer on 7/22/2015.
 */
public interface ErrorBundle {

    Exception getException();
    String getErrorMessage();

}