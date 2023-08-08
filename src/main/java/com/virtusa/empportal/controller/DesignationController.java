package com.virtusa.empportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virtusa.empportal.model.Designation;
import com.virtusa.empportal.service.DesignationService;
import com.virtusa.empportal.utils.Response;

@RestController
@RequestMapping(path = "/designation")
public class DesignationController {
	
	@Autowired
	private DesignationService designationService;

	@PostMapping
	public Response addDesignation(@RequestBody Designation designation) {
		return designationService.addDesignation(designation);
	}
	
	@GetMapping
	public Response getDesignations() {
		return designationService.getDesignations();
	}
}
