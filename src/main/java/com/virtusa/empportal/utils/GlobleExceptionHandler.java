package com.virtusa.empportal.utils;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobleExceptionHandler {
	
	@ExceptionHandler(value = {NullPointerException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody Response handleException1(Exception ex) {
		return new Response(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), null);
	}
	
	@ExceptionHandler(value = {ConstraintViolationException.class, MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody Response handleException2(Exception ex) {
		return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), null);
	}

}
