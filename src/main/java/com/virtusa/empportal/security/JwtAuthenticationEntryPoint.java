package com.virtusa.empportal.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.virtusa.empportal.utils.Response;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");
		Response msg = new Response(LocalDateTime.now(), HttpStatus.UNAUTHORIZED, authException.getMessage(), null);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		
		Map<String, Object> body = mapper.convertValue(msg, HashMap.class);
		body.replace("timeStamp", msg.getTimeStamp().toString());
		
		mapper.writeValue(response.getOutputStream(), body);
		
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//response.flushBuffer();
	}

}
