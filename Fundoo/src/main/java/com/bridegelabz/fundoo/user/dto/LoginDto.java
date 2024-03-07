package com.bridegelabz.fundoo.user.dto;

public class LoginDto {

		private String emailId;
		private String password;
		public LoginDto() 
		{
			
		}
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		@Override
		public String toString() {
			return "LoginDto [emailId=" + emailId + ", password=" + password + "]";
		}
		
}
