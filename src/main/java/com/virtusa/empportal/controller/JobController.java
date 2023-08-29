package com.virtusa.empportal.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.virtusa.empportal.model.JobVacancy;
import com.virtusa.empportal.service.JobService;
import com.virtusa.empportal.utils.ReferanceStatus;
import com.virtusa.empportal.utils.Response;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/jobVacancy")
public class JobController {
	
	@Autowired
	JobService jobService;
	
	@GetMapping
	public Response getAllJobVacancy() {
		return jobService.getAllJobVacancy();
	}
	
	@GetMapping(path = "/{id}")
	public Response getJobVacancyById(@PathVariable String id) {
		try {
			int jobID = Integer.parseInt(id);
			return jobService.getJobVacancyByID(jobID);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}
	
	@GetMapping(path = "/hr/{id}/references")
	public Response getAllJobReferences(@PathVariable String id) {
		try {
			int jobID = Integer.parseInt(id);
			return jobService.getAllJobReferencesByID(jobID);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}

	@GetMapping(path = "/hr/reference/{id}")
	public Response getReference(@PathVariable String id, HttpServletResponse res) {
		try {
			int referanceId = Integer.parseInt(id);
			return jobService.getReferenceById(referanceId);
		} catch(NumberFormatException exception) {

			res.setStatus(400);
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}
	
	@GetMapping(path = "/hr/reference/{id}/resume")
	public ResponseEntity<Object> getReferenceResume(@PathVariable String id) {
//		ResponseEntity<Object> re = new ResponseEntity<Object>()
		try {
			int referanceId = Integer.parseInt(id);
			Object data = jobService.getReferenceResume(referanceId);
		
			if(data instanceof byte[]) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(data);
			} else {
				return ResponseEntity.ok().body(data);
			}
		} catch(NumberFormatException exception) {
			return ResponseEntity.badRequest().body(new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null));
		}
	}
	
	@PatchMapping(path = "/hr/reference/{id}")
	public Response updateReferenceStatus(@PathVariable String id, @RequestParam ReferanceStatus status) {
		try {
			int referanceId = Integer.parseInt(id);
			return jobService.updateJobReferenceStatus(referanceId, status);
		} catch(NumberFormatException exception) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
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
