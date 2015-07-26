package com.tamerbarsbay.depothouston.presentation.exception;

import android.content.Context;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.data.exception.NetworkConnectionException;
import com.tamerbarsbay.depothouston.data.exception.StopNotFoundException;

/**
 * Created by Tamer on 7/23/2015.
 */
public class ErrorMessageFactory {

    private ErrorMessageFactory() {
        //empty
    }

    /**
     * Creates a String representing an error message.
     *
     * @param context Context needed to retrieve string resources.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return {@link String} an error message.
     */
    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (exception instanceof StopNotFoundException) {
            message = context.getString(R.string.exception_message_stop_not_found);
        } else {
            message = exception.getMessage();
        }

        return message;
    }
}