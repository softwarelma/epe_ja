package com.softwarelma.epe.p1.app;

public final class EpeAppRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EpeAppRuntimeException(String message) {
        super(message);
        EpeAppLogger.log(this);
    }

    public EpeAppRuntimeException(String message, Throwable cause) {
        super(message, cause);
        EpeAppLogger.log(this);
    }

}
