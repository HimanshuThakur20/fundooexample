package com.bridegelabz.fundoo.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridegelabz.fundoo.exception.CreateNoteExceptions;
import com.bridegelabz.fundoo.response.Response;
import com.bridegelabz.fundoo.util.StatusHelper;

public class ExceptionHandler 
{
	@org.springframework.web.bind.annotation.ExceptionHandler(value = CreateNoteExceptions.class)
	public ResponseEntity<Response> createNoteExceptionHandler(CreateNoteExceptions createNoteExceptions)
	{
		Response response = StatusHelper.statusInfo(createNoteExceptions.getMessage(), createNoteExceptions.getErrorCode());
		return new ResponseEntity<Response> (response , HttpStatus.OK);
		
	}
}
