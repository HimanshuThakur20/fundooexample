package com.bridegelabz.fundoo.exception;

public class CreateNoteExceptions extends RuntimeException
{
	private String message;
	private int errorCode;
	public CreateNoteExceptions(String message, int errorCode) {
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
		return "CreateNoteExceptions [message=" + message + ", errorCode=" + errorCode + "]";
	}
	
}
