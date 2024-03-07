package com.bridegelabz.fundoo.exception;

public class RegistrationExceptions extends RuntimeException
{
	private String message;
	private int errorCode;
	public RegistrationExceptions(String meassage, int errorCode) {
		this.message = meassage;
		this.errorCode = errorCode;
		
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String meassage) {
		this.message = meassage;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	@Override
	public String toString() {
		return "RegistrationExceptions [meassage=" + message + ", errorCode=" + errorCode + "]";
	}
	
}
