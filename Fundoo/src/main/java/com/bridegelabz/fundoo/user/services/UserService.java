package com.bridegelabz.fundoo.user.services;
import com.bridegelabz.fundoo.util.*;
import ch.qos.logback.classic.pattern.Util;
import ch.qos.logback.core.encoder.Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridegelabz.fundoo.exception.LoginExceptions;
import com.bridegelabz.fundoo.exception.RegistrationExceptions;
import com.bridegelabz.fundoo.response.Response;
import com.bridegelabz.fundoo.response.ResponseToken;
import com.bridegelabz.fundoo.user.dto.LoginDto;
import com.bridegelabz.fundoo.user.dto.UserDto;
import com.bridegelabz.fundoo.user.model.Email;
import com.bridegelabz.fundoo.user.model.User;
import com.bridegelabz.fundoo.user.repository.UserRepository;
import java.nio.file.FileSystems;
/**
 * Purpose: To Provide Service to the Rest Controller
 * @author Ahetesham Ahamad
 *
 */
@Service
@Transactional
@PropertySource("classpath:message.properties")
public class UserService implements IUserService
{
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private EmailService emailService;
	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private Response response = null;
	@Autowired
	private ResponseToken responseToken = null;
	@Autowired
	private Environment environment;
	@Autowired
	private GenerateMail generateMail;
	private final Path location = (Path) Paths.get("/home/admin1/fundooImages");
	/**
	 * purpose: To Register and save user
	 * @throws RegistrationExceptions 
	 * @throws UnsupportedEncodingException 
	 * @throws IllegalArgumentException 
	 */
	public Response saveMyUser(UserDto userDto) throws RegistrationExceptions, IllegalArgumentException, UnsupportedEncodingException
	{
		Email email = new Email();
		System.out.println("Inside save my user fun");
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));  
		User user = modelMapper.map(userDto, User.class);
		Optional<User> userExist = userRepository.findByEmailId(user.getEmailId());
		if(userExist.isPresent())
		{
			System.out.println("User alrady Exist");
			throw new RegistrationExceptions(environment.getProperty("status.register.userAlreadyExist"), Integer.parseInt(environment.getProperty("status.token.ErrorCode")));
		}
		else
		{
			System.out.println("Inside else to save");
			user.setRegisteredDate(LocalDate.now());
			userRepository.save(user);
			System.out.println("User saved");
			email.setFrom("aheteshams007@gmail.com");
			email.setTo(user.getEmailId());
			email.setSubject("User Verification");
			email.setBody("http://localhost:4200/login/"+UserToken.generateToken(user.getId()));
//			emailService.sendMail(email);
			generateMail.send(email);
			System.out.println("Mail sent");
			return StatusHelper.statusInfo(environment.getProperty("status.mail.mailSent"), Integer.parseInt(environment.getProperty("status.mail.mailSentCode")));
		}	
	}
	/**
	 * Purpose: To verify user using email
	 */
	public Response verification(String token) throws UnsupportedEncodingException 
	{
		int id = UserToken.tokenVerify(token);
		System.out.println("id:"+id);
		Optional<User> user = userRepository.findById(id);
		System.out.println("User : "+ user.get().toString());
		user.get().setVerified(true);
		user.get().setModifiedDate(LocalDate.now());
		System.out.println("User : "+ user.get().toString());
		userRepository.save(user.get());
		return StatusHelper.statusInfo(environment.getProperty("status.register.successRegistered"), Integer.parseInt(environment.getProperty("status.register.successCode")));
	}
	/**
	 * Purpose: To Change the password of of Exiting User 
	 */
	public Response changePasswordMailSender(String emailId) throws IllegalArgumentException, UnsupportedEncodingException 
	{
		Email email = new Email();
		email.setTo(emailId);
		email.setSubject("Change Password");
		Optional<User> user = userRepository.findByEmailId(emailId);
		if(user.isPresent())
		{
			email.setBody("http://localhost:4200/setPassword/"+UserToken.generateToken(user.get().getId()));
			emailService.sendMail(email);
			return StatusHelper.statusInfo(environment.getProperty("status.mail.mailSent"), Integer.parseInt(environment.getProperty("status.mail.mailSentCode")));
		}
		else
		{
			throw new RegistrationExceptions(environment.getProperty("status.userNotExist"), Integer.parseInt(environment.getProperty("status.token.ErrorCode")));
		}
		
	}
	/**
	 * Purpose: To Set the the new Password of Existing User
	 */
	public Response setNewPasswordService(String newPassword, String token) throws UnsupportedEncodingException 
	{
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent())
		{
			user.get().setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user.get());	
			return response = StatusHelper.statusInfo(environment.getProperty("status.setPassword"), Integer.parseInt(environment.getProperty("status.register.successCode")));//New Password Set
		}
		else
		{
			throw new RegistrationExceptions(environment.getProperty("status.userNotExist"), Integer.parseInt(environment.getProperty("status.token.ErrorCode")));
		}
	}
	/**
	 * Purpose: To check whether user is present or not and make him logged in
	 */
	public ResponseToken loginService(LoginDto loginDto)
	{
		String token=null;
		Optional<User> user = userRepository.findByEmailId(loginDto.getEmailId());
		int id = user.get().getId();
		String name = user.get().getName();
		String emailId = user.get().getEmailId();
		if(user.isPresent())
		{
			if(passwordEncoder.matches(loginDto.getPassword(), user.get().getPassword()))
			{
				try {
					token = UserToken.generateToken(id);
				} catch (IllegalArgumentException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return StatusHelper.loginStatus(environment.getProperty("status.login.success"), Integer.parseInt(environment.getProperty("status.login.successCode")), token, name, emailId);
			}
			else
			{
				throw new LoginExceptions(environment.getProperty("status.login.falure"), token, Integer.parseInt(environment.getProperty("status.login.falureCode")));
			}
		}
		else
		{ 
			return StatusHelper.loginStatus(environment.getProperty("status.userNotExist"),Integer.parseInt(environment.getProperty("status.login.falureCode")),token, name, emailId);
		}
	}
	public Response uploadImage(String token, MultipartFile picture) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		System.out.println(id);
		Optional<User> user = userRepository.findById(id);
		
		if(!user.isPresent())
		{
			throw new RegistrationExceptions(environment.getProperty("status.userNotExist"), Integer.parseInt(environment.getProperty("status.token.ErrorCode")));
		}
		
		UUID uuid = UUID.randomUUID();
		String imageId = uuid.toString();
		
		try
		{       System.out.println(picture);
 			Files.copy(picture.getInputStream(), location.resolve(imageId), StandardCopyOption.REPLACE_EXISTING);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		user.get().setProfilePic(imageId);
		userRepository.save(user.get());
		return StatusHelper.statusInfo("Image Uploaded successfully", Integer.parseInt(environment.getProperty("status.register.successCode")));
		
	}
	public Resource getUploadedImage(String token) throws UnsupportedEncodingException
	{
		int id = UserToken.tokenVerify(token);
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
		{
			throw new RegistrationExceptions(environment.getProperty("status.userNotExist"), Integer.parseInt(environment.getProperty("status.token.ErrorCode")));
		}
		Path imagePath = location.resolve(user.get().getProfilePic());
		try
		{
			Resource resource = new UrlResource(imagePath.toUri());
			if(resource.exists() || resource.isReadable())
			{
				System.out.println(resource); 
				return resource;
			}
		}
		catch (MalformedURLException e) 
		{
			 e.printStackTrace();
		}
		return null;
	
	}
	
}
