package com.bridegelabz.fundoo.exception;

public class CreateLabelExceptions extends RuntimeException
{
	private String message;
	private int errorCode;
	
	public CreateLabelExceptions(String message, int errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	@Override
	public String toString() {
		return "CreateLabelExceptions [message=" + message + ", errorCode=" + errorCode + "]";
	}
	
}
