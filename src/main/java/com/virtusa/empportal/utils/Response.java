package com.virtusa.empportal.utils;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatusCode;

import lombok.Data;

@Data
public class Response {

	private final LocalDateTime timeStamp;
	private final HttpStatusCode status;
	private final String message;
	private final Object body;
}
