package com.virtusa.empportal.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virtusa.empportal.model.JobVacancy;
import com.virtusa.empportal.service.JobService;
import com.virtusa.empportal.utils.Response;

@RestController
@RequestMapping("/jobVacancy")
public class JobController {
	
	@Autowired
	JobService jobService;
	
	@GetMapping
	public Response getAllJobVacancy() {
		return jobService.getAllJobVacancy();
	}

	@PostMapping(path = "/hr/add")
	public Response addJob(@RequestBody JobVacancy jobVacancy) {
		return jobService.addJobVacancy(jobVacancy);
	}
	
	@PutMapping(path = "/hr/update")
	public Response updateJob(@RequestBody JobVacancy jobVacancy) {
		return jobService.updateJobVacancy(jobVacancy);
	}
	
	@PatchMapping(path = "/hr/{id}/fulfilled")
	public Response changeJobStatus(@PathVariable String id) {
		try {
			int jobID = Integer.parseInt(id);
			return jobService.closeJobVacancy(jobID);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}
}
