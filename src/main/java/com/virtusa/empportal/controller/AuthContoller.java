package com.virtusa.empportal.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtusa.empportal.model.FileStorage;
import com.virtusa.empportal.service.EmployeeService;
import com.virtusa.empportal.service.FileService;
import com.virtusa.empportal.utils.Response;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthContoller {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private FileService fileService;

	@GetMapping(path = "/setRoles")
	public Response setRoles(HttpServletResponse res) {
		res.setStatus(200);
		 return employeeService.addUserRoles();
	}
	
	@GetMapping("/filestorage/{id}")
	public ResponseEntity<Object> showfile(@PathVariable String id) {
//		ResponseEntity<Object> re = new ResponseEntity<Object>()
		try {
			int fileId = Integer.parseInt(id);
			Object data = fileService.getFile(fileId);
		
			if(data instanceof FileStorage) {
				return ResponseEntity.ok().contentType(MediaType.parseMediaType(((FileStorage) data).getFileType())).body(((FileStorage) data).getFile());
			} else {
				return ResponseEntity.ok().body(data);
			}
		} catch(NumberFormatException exception) {
			return ResponseEntity.badRequest().body(new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null));
		}
	}
	
	@GetMapping(path = "/login")
	public Response loginUser(@RequestParam String email, @RequestParam String password, HttpServletResponse res) {
		res.setStatus(200);
		return employeeService.loginEmployee(email, password);
	}
}
