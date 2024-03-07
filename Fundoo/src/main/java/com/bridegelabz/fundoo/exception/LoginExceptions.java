package com.bridegelabz.fundoo.exception;

public class LoginExceptions extends RuntimeException
{
	private String message;
	private String token;
	private int errorCode;
	
	public LoginExceptions(String message, String token, int errorCode) {
		this.message = message;
		this.token = token;
		this.errorCode = errorCode;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
		return "LoginExceptions [message=" + message + ", errorCode=" + errorCode + "]";
	}
	
}
