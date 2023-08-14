package com.virtusa.empportal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.virtusa.empportal.model.Employees;
import com.virtusa.empportal.model.JobVacancy;
import com.virtusa.empportal.model.Reference;
import com.virtusa.empportal.repository.EmployeeRepo;
import com.virtusa.empportal.repository.JobVacancyRepo;
import com.virtusa.empportal.repository.ReferenceRepo;
import com.virtusa.empportal.utils.Response;

@Service
public class JobService {

	@Autowired
	private JobVacancyRepo jobVacancyRepo;
	@Autowired
	private ReferenceRepo referenceRepo;
	@Autowired
	private EmployeeRepo employeeRepo;

	public Response addJobVacancy(JobVacancy jobVacancy) {
//		JobVacancy job = jobVacancyRepo.findByTitleAndFulfilled(jobVacancy.getTitle(), false).orElse(null);
//		if(job != null) {
//			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Job already registered", job);
//		}
		JobVacancy job = jobVacancyRepo.save(jobVacancy);
		return new Response(LocalDateTime.now(), HttpStatus.CREATED, "Job registered", job);
		
	}
	
	public Response getAllJobVacancy() {
		List<JobVacancy> jobVacancies = jobVacancyRepo.findAll(); 
		return new Response(LocalDateTime.now(), HttpStatus.OK, "All available job opportunites feched", jobVacancies);
	}
	
	public Response updateJobVacancy(JobVacancy jobVacancy) {
		JobVacancy job = jobVacancyRepo.findById(jobVacancy.getJobID()).orElse(null);
		if(job == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Job not found", job);
		}
		job = jobVacancyRepo.save(jobVacancy);
		return new Response(LocalDateTime.now(), HttpStatus.OK, "Job opportunity updated", job);
	}
	
	public Response closeJobVacancy(int id) {
		JobVacancy job = jobVacancyRepo.findByJobIDAndFulfilled(id, false).orElse(null);
		if(job == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Job not found", job);
		}
		job.setFulfilled(true);
		JobVacancy updatedJob = jobVacancyRepo.save(job);
		return new Response(LocalDateTime.now(), HttpStatus.CREATED, "Job opportunity marked as fulfilled", updatedJob);
	}

	public Response giveReference(int employeeId, Reference reference) {
		JobVacancy job = jobVacancyRepo.findById(reference.getJob().getJobID()).orElse(null);
		if(job == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Job not found", job);
		}
		reference.setJob(job);
		
		Employees emp = employeeRepo.findById(employeeId).orElse(null);
		if(emp == null) {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Employee not found", "ERROR");
		}
		reference.setEmployee(emp);
		Reference newReference = referenceRepo.save(reference);
		return new Response(LocalDateTime.now(), HttpStatus.CREATED, "Reference Created", newReference);
	}
	
}
