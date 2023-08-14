package com.virtusa.empportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtusa.empportal.service.EmployeeService;
import com.virtusa.empportal.utils.Response;

@RestController
@RequestMapping("/auth")
public class AuthContoller {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping(path = "/setRoles")
	public Response setRoles() {
		return employeeService.addUserRoles();
	}
	
	@GetMapping(path = "/login")
	public Response loginUser(@RequestParam String email, @RequestParam String password) {
		return employeeService.loginEmployee(email, password);
	}
}
