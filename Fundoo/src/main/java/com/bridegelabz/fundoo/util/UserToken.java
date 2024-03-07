package com.bridegelabz.fundoo.util;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

@Component
public class UserToken {
	private static String TOKEN;
	public UserToken() 
	{
	
	}
	public static String generateToken(long id) throws IllegalArgumentException, UnsupportedEncodingException {
		TOKEN="Ahetesham";
		Algorithm algorithm = Algorithm.HMAC256(TOKEN);
		String token=JWT.create().withClaim("ID", id).sign(algorithm);
		return token;		
	}
	
	public static int tokenVerify(String token) throws UnsupportedEncodingException{
		TOKEN="Ahetesham";

		int userid;
		Verification verification = null;
		try {
			verification = JWT.require(Algorithm.HMAC256(UserToken.TOKEN));
			System.out.println("Algorithm stored");
			} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		}
		JWTVerifier jwtverifier=verification.build();
		DecodedJWT decodedjwt=jwtverifier.verify(token);
		Claim claim=decodedjwt.getClaim("ID");
		userid=claim.asInt();	
		return userid;
	}
}
