package com.virtusa.empportal.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtusa.empportal.model.Employees;
import com.virtusa.empportal.model.EmployeesLeaves;
import com.virtusa.empportal.model.Leaves;
import com.virtusa.empportal.model.Messages;
import com.virtusa.empportal.service.EmailService;
import com.virtusa.empportal.service.EmployeeService;
import com.virtusa.empportal.service.LeavesService;
import com.virtusa.empportal.service.MessageService;
import com.virtusa.empportal.utils.Response;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private LeavesService leavesService;

	@GetMapping("/mail")
	public Response sendmail(){
		return emailService.sendSimpleMessage("chinmayi@virtusa.com", "Hello", "Hello");
		
	}
	
	@GetMapping("/all")
	public Response getAllEmployeeList(){
		List<Employees> empList = employeeService.getAllEmp();
		return new Response(LocalDateTime.now(), HttpStatus.OK, "All employee list fetched", empList);
		
	}
	
	@PostMapping("/addEmp")
	public Response addEmployee(@RequestBody Employees employee){
		return employeeService.addEmployeeInfo(employee);
	}
	
	@GetMapping("/profile/{id}")
	public Response getEmployeeById(@PathVariable String id){
		try {
			int employeeId = Integer.parseInt(id);
			return employeeService.getEmpById(employeeId);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
		
	}
	
	@PatchMapping("/delete/{id}")
	public Response removeEmployee(@PathVariable String id){
		try {
			int employeeId = Integer.parseInt(id);
			return employeeService.removeEmp(employeeId);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}
	
	@PutMapping("/update")
	public Response updateEmployee(@Valid @RequestBody Employees employee) {
		return employeeService.updateEmployee(employee);
		
	}
	
	@PostMapping("/messages/send")
	public Response sendMessage(@RequestBody Messages msg) {
		return messageService.sendMessage(msg);
	}
	
	@GetMapping("/{id}/messages")
	public Response getMessages(@PathVariable String id) {
		try {
			int employeeId = Integer.parseInt(id);
			return employeeService.getAllMessages(employeeId);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}
	
	@GetMapping("/{id}/leaves")
	public Response seeAllLeaves(@PathVariable String id) {
		try {
			int employeeId = Integer.parseInt(id);
			return leavesService.getEmployeeLeavesInfo(employeeId);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}
	
	@PostMapping("/{id}/leaves/apply")
	public Response applyForLeaves(@PathVariable String id,@RequestBody EmployeesLeaves employeesLeaves) {
		try {
			int employeeId = Integer.parseInt(id);
			return leavesService.applyForLeaves(employeeId, employeesLeaves);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}
	
	@GetMapping("/{id}/leaves/overboundReport")
	public Response getEmployeeOverboundReport(@PathVariable String id) {
		try {
			int employeeId = Integer.parseInt(id);
			return leavesService.generateEmployeeOverboundReport(employeeId);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}
	

}

//<dependency>
//<groupId>org.springframework.security</groupId>
//<artifactId>spring-security-web</artifactId>
//<version>6.1.2</version>
//</dependency>
//<dependency>
//<groupId>org.springframework.security</groupId>
//<artifactId>spring-security-core</artifactId>
//<version>6.1.2</version>
//</dependency>
//<dependency>
//<groupId>org.springframework.security</groupId>
//<artifactId>spring-security-config</artifactId>
//<version>6.1.2</version>
//</dependency>