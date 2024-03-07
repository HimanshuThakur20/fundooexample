package com.bridegelabz.fundoo.user.services;

import java.io.UnsupportedEncodingException;

import com.bridegelabz.fundoo.exception.RegistrationExceptions;
import com.bridegelabz.fundoo.response.Response;
import com.bridegelabz.fundoo.response.ResponseToken;
import com.bridegelabz.fundoo.user.dto.LoginDto;
import com.bridegelabz.fundoo.user.dto.UserDto;

public interface IUserService 
{
	public Response saveMyUser(UserDto userDto) throws IllegalArgumentException, UnsupportedEncodingException, RegistrationExceptions;
	public Response verification(String token) throws UnsupportedEncodingException;
	public Response changePasswordMailSender(String emailId) throws IllegalArgumentException, UnsupportedEncodingException;
	public Response setNewPasswordService(String newPassword, String token) throws UnsupportedEncodingException;
	public ResponseToken loginService(LoginDto loginDto);
	
}
