package com.virtusa.empportal.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virtusa.empportal.model.Department;
import com.virtusa.empportal.service.DepartmentService;
import com.virtusa.empportal.utils.Response;

@RestController
@RequestMapping(path = "/department")
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;

	@PostMapping("/addDepartment")
	public Response addDepartment(@RequestBody Department department) {
		return departmentService.addDepartment(department);
	}
	
	@GetMapping()
	public Response getDepartments() {
		return departmentService.getAllDepartments();
	}
	
	@DeleteMapping("/remove/{id}")
	public Response deleteDepartment(@PathVariable String id) {
		try {
			int employeeId = Integer.parseInt(id);
			return departmentService.softDeleteDepartment(employeeId);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}
	
	
}
