package com.bridegelabz.fundoo.util;

import com.bridegelabz.fundoo.response.Response;
import com.bridegelabz.fundoo.response.ResponseToken;

public class StatusHelper 
{
	public static Response statusInfo(String message, int code)
	{
		Response response = new Response();
		response.setMessage(message);
		response.setCode(code);
		return response;
		
	}
	
	public static ResponseToken tokenStatusinfo(String message, int code, String token)
	{
		ResponseToken responseToken = new ResponseToken();
		responseToken.setMessage(message);
		responseToken.setCode(code);
		responseToken.setToken(token);
		return responseToken;
		
	}
	public static ResponseToken loginStatus(String message, int code, String token, String name, String emailId)
	{
		ResponseToken loginResponseToken = new ResponseToken();
		loginResponseToken.setMessage(message);
		loginResponseToken.setCode(code);
		loginResponseToken.setToken(token);
		loginResponseToken.setName(name);
		loginResponseToken.setEmailId(emailId);
		return loginResponseToken;
	}
	public static ResponseToken statusResponseInfo(String message, int code)
	{
		ResponseToken responseToken = new ResponseToken();
		responseToken.setMessage(message);
		responseToken.setCode(code);
		return responseToken;
		
	}
}
