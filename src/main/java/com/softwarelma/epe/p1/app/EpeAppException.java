package com.softwarelma.epe.p1.app;

public final class EpeAppException extends Exception {

	private static final long serialVersionUID = 1L;

	public EpeAppException(String message) {
		super(message);
		EpeAppLogger.log(this);
	}

	public EpeAppException(String message, Throwable cause) {
		super(message, cause);
		EpeAppLogger.log(this);
	}

}
