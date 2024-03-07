package com.bridegelabz.fundoo.exceptionHandler;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.bridegelabz.fundoo.Exception.LoginExceptions;
import com.bridegelabz.fundoo.Exception.RegistrationExceptions;
import com.bridegelabz.fundoo.response.Response;
import com.bridegelabz.fundoo.response.ResponseToken;
import com.bridegelabz.fundoo.util.StatusHelper;

@RestControllerAdvice
public class ExceptionHandler 
{
	@org.springframework.web.bind.annotation.ExceptionHandler(value = RegistrationExceptions.class)
	public ResponseEntity<Response> registrationExceptionHandler(RegistrationExceptions registrationExceptions)
	{
		Response response = StatusHelper.statusInfo(registrationExceptions.getMessage(), registrationExceptions.getErrorCode());
		return new ResponseEntity<Response> (response , HttpStatus.OK);
		
	}
	@org.springframework.web.bind.annotation.ExceptionHandler(value = LoginExceptions.class)
	public ResponseEntity<ResponseToken> loginExceptionHandler(LoginExceptions loginExceptions)
	{
		ResponseToken responseToken = StatusHelper.tokenStatusinfo(loginExceptions.getMessage(),loginExceptions.getErrorCode(), loginExceptions.getToken());
		return new ResponseEntity<ResponseToken> (responseToken , HttpStatus.OK);
	}
	
}
