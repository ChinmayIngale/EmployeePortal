package com.virtusa.empportal.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtusa.empportal.model.Employees;
import com.virtusa.empportal.model.JobVacancy;
import com.virtusa.empportal.model.Reference;
import com.virtusa.empportal.repository.EmployeeRepo;
import com.virtusa.empportal.repository.JobVacancyRepo;
import com.virtusa.empportal.repository.ReferenceRepo;
import com.virtusa.empportal.utils.ReferanceStatus;
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
		return new Response(LocalDateTime.now(), HttpStatus.OK, "All available job opportunites fetched", jobVacancies);
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

	public Response giveReference(String empId, String referenceData, MultipartFile resume) {

		ObjectMapper objectMapper = new ObjectMapper();
        Reference reference= null;
        try {
			reference = objectMapper.readValue(referenceData, Reference.class);
			int employeeId = Integer.parseInt(empId);
			
			reference.setResume(resume.getBytes());
			
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
			
			if(resume.getSize() > 2097152) {
				float sizeinKB = resume.getSize()/1024f;
				if(sizeinKB < 1024) {
					return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "file size must be lower than 2MB", "Current file size is: "+sizeinKB+"KB");
				} else {
					float sizeinMB = sizeinKB/1024;
					return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "file size must be lower than 2MB", "Current file size is: "+sizeinMB+"MB");
				}
			}
			
			Reference newReference = referenceRepo.save(reference);
			return new Response(LocalDateTime.now(), HttpStatus.CREATED, "Reference Created", newReference);
	
		}  catch (JsonProcessingException e) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert referanceData to Reference object", e.getMessage());
		} catch(NumberFormatException e) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+empId+" to integer", e.getMessage());
		} catch (IOException e) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not save resume", e.getMessage());
		}
	
	}

	public Response getJobVacancyByID(int jobID) {
		JobVacancy job = jobVacancyRepo.findById(jobID).orElse(null);
		if(job == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Job not found", job);
		}
		return new Response(LocalDateTime.now(), HttpStatus.OK, "Job opportunity fetched", job);
	}

	public Response getAllJobReferencesByID(int jobID) {
		JobVacancy job = jobVacancyRepo.findById(jobID).orElse(null);
		if(job == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Job not found", job);
		}
		return new Response(LocalDateTime.now(), HttpStatus.OK, "All references for Job '"+job.getTitle()+"' feched", job.getReferences());
	}
	
	public Response getAllPeopleReferredByEmployee(int employeeId) {
		Employees employee = employeeRepo.findById(employeeId).orElse(null);
		if(employee == null) {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Employee not found", null);
		}
		
		Set<Reference> references = referenceRepo.findAllByEmployee(employee);
		Set<Map<String, Object>> peopleData = new HashSet<>();
		references.forEach(ref -> {
			Map<String, Object> people = new HashMap<>();
			people.put("referenceId", ref.getReferenceID());
			people.put("name", ref.getFirstName()+" "+ref.getLastName());
			people.put("email", ref.getEmail());
			peopleData.add(people);
		});
		Map<String, Object> data = new HashMap<>();
		data.put("numberOfPeople", references.size());
		data.put("people", peopleData);
		return new Response(LocalDateTime.now(), HttpStatus.OK, "Data fetched", data);
	}

	public Response updateJobReferenceStatus(int referanceId, ReferanceStatus status) {
		Reference reference = referenceRepo.findById(referanceId).orElse(null);
		if(reference == null) {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Job referance not found", null);
		}
		reference.setStatus(status);
		Reference newrReference = referenceRepo.save(reference);
		return new Response(LocalDateTime.now(), HttpStatus.OK, "Job referance status updated", newrReference);
		
	}

	public Response getReferenceById(int referanceId) {
		Reference reference = referenceRepo.findById(referanceId).orElse(null);
		if(reference == null) {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Job referance not found", null);
		}
		return new Response(LocalDateTime.now(), HttpStatus.OK, "Job referance fetched", reference);
		
	}

	public Object getReferenceResume(int referanceId) {
		Reference reference = referenceRepo.findById(referanceId).orElse(null);
		if(reference == null) {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Job referance with id "+referanceId+" not found", null);
		}
		return reference.getResume();
	}


}
