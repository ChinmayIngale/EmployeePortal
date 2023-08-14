package com.virtusa.empportal.security;

import java.security.Key;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenManager {
	
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	public String generateJwtToken(Authentication authentication) {
		
		return Jwts.builder()
							.setSubject(authentication.getName())
							.setIssuedAt(new Date())
							.setExpiration(new Date(new Date().getTime() + 60*1000*10))
							.signWith(key, SignatureAlgorithm.HS512)
							.compact();
	}
	
	public String getUsernameFromJwtToken(String token){
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect",ex.fillInStackTrace());
		}
	}
}
