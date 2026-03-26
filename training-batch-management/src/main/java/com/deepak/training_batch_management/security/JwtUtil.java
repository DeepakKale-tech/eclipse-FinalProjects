package com.deepak.training_batch_management.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {

	private final String SECRET = "mysecretkeyoffinalprojectmysecrectkey";
	
	private Key getkey()
	{
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}
	
	public String generateToken(String email)
	{
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.signWith(getkey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractEmail(String token)
	{
		return Jwts.parserBuilder()
				.setSigningKey(getkey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public boolean validateToken(String token)
	{
		try
		{
			Jwts.parserBuilder().setSigningKey(getkey()).build().parseClaimsJws(token);
			return true;
		}catch(Exception e)
		{
			return false;
		}
	}
}
