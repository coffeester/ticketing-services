package com.coffeester.ticketing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by amitsehgal on 1/30/16.
 */
@ControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(LevelDoesntExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public
    @ResponseBody
    ErrorResponseBody handleCustomizedExceptionIvalidDeviceCert(
            LevelDoesntExistsException ex, WebRequest request) {
        final HttpStatus unprocessableEntityStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponseBody errorResponseBody = new ErrorResponseBody();
        errorResponseBody.setMessage(ex.getMessage());
        errorResponseBody.setStatus(unprocessableEntityStatus.value());
        return errorResponseBody;

    }

}
