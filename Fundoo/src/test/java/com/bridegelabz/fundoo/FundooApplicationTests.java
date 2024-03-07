package com.bridegelabz.fundoo;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridegelabz.fundoo.user.dto.LoginDto;
import com.bridegelabz.fundoo.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FundooApplicationTests {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext wac;
	@InjectMocks
	private UserDto userDto;
	@InjectMocks
	private LoginDto loginDto;
	@Before(value = "")
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
	@Test
	public void contextLoads() {
		
	}
	@Test
	public void RegisterTest() throws Exception
	{
		userDto.setName("Ahetesham");
		userDto.setEmailId("aheteshams007@gmail.com");
		userDto.setPhNumber("9623886949");
		userDto.setPassword("123456789");
		mvc.perform( MockMvcRequestBuilders
			      .post("/saveuser")
			      .content(asJsonString(userDto))
			      .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
			      .accept(org.springframework.http.MediaType.APPLICATION_JSON))
			  	  .andDo(print())
			      .andExpect(status().isOk());
	}
	@Test
	public void LoginTest() throws Exception {
		loginDto.setEmailId("aheteshams007@gmail.com");
		loginDto.setPassword("123456789");
		mvc.perform( MockMvcRequestBuilders
			      .post("/login")
			      .content(asJsonString(loginDto))
			      .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
			      .accept(org.springframework.http.MediaType.APPLICATION_JSON))
			  	  .andDo(print())
			      .andExpect(status().isOk());
	}
}
