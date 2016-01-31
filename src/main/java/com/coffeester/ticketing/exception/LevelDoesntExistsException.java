package com.coffeester.ticketing.exception;

/**
 * Created by amitsehgal on 1/30/16.
 */
public class LevelDoesntExistsException extends RuntimeException {

    private ExceptionCode exceptionCode;
    private String resourceId;

    public LevelDoesntExistsException(ExceptionCode exceptionCode, String id) {
        super(id);
        this.resourceId = id;
        this.exceptionCode = exceptionCode;
    }

    public LevelDoesntExistsException(ExceptionCode exceptionCode, String id, String internalMessage) {
        super("ID: " + id + " Message: " + internalMessage);
        this.resourceId = id;
        this.exceptionCode = exceptionCode;
    }

    public LevelDoesntExistsException(ExceptionCode exceptionCode, String id, String internalMessage, Throwable cause) {
        super("ID: " + id + " Message: " + internalMessage, cause);
        this.exceptionCode = exceptionCode;
        this.resourceId = id;
    }

    public LevelDoesntExistsException(ExceptionCode exceptionCode, String id, Throwable cause) {
        super(id, cause);
        this.exceptionCode = exceptionCode;
        this.resourceId = id;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public String getResourceId() {
        return resourceId;
    }

    @Override
    public String getMessage() {
        return exceptionCode.getClientFacingMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return exceptionCode.getClientFacingMessage();
    }


}
