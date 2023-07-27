package com.yang.auto.util;

public class UserFriendlyException extends RuntimeException {

	public UserFriendlyException() {
	}

	public UserFriendlyException(String message) {
		super(message);
	}

	public UserFriendlyException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserFriendlyException(Throwable cause) {
		super(cause);
	}

	public UserFriendlyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	private int httpStatusCode = 500;
	/**
	 * 错误码。取值参考{@link ErrorCode#getCode()}
	 */
	private int errorCode = ErrorCode.serverError.getCode();

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public UserFriendlyException setHttpStatusCode(int statusCode) {
		this.httpStatusCode = statusCode;
		return this;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public UserFriendlyException setErrorCode(int errorCode) {
		this.errorCode = errorCode;
		return this;
	}
}
