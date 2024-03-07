package com.bridegelabz.fundoo.user.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserDto 
{
	@NotEmpty(message="Name Cannot Not be Empty")
	@NotNull(message="Name Cannot Not be null")
	private String name;
	@NotEmpty(message="EmailId Cannot Not be Empty")
	@NotNull(message="EmailId Cannot Not be null")
	private String emailId;
	@NotEmpty(message="PhNumber Cannot Not be Empty")
	@NotNull(message="PhNumber Cannot Not be null")
	private String phNumber;
	@NotEmpty(message="Password Cannot Not be Empty")
	@NotNull(message="Password Cannot Not be null")
	private String password;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPhNumber() {
		return phNumber;
	}
	public void setPhNumber(String phNumber) {
		this.phNumber = phNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "UserDto [name=" + name + ", emailId=" + emailId + ", phNumber=" + phNumber + ", password=" + password
				+ "]";
	} 
	
}
