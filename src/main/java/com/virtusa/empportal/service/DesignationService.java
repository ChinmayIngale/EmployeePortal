package com.virtusa.empportal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.virtusa.empportal.model.Address;
import com.virtusa.empportal.model.Department;
import com.virtusa.empportal.model.Designation;
import com.virtusa.empportal.repository.DesignationRepo;
import com.virtusa.empportal.utils.Response;

@Service
public class DesignationService {
	
	@Autowired
	private DesignationRepo designationRepo;

	public Response addDesignation(Designation designation) {
		if(designationRepo.existsByDesignationName(designation.getDesignationName())) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST,"Designation with same name exists", null);
		}
		
		Designation newDesignation = designationRepo.save(designation);
		return new Response(LocalDateTime.now(), HttpStatus.CREATED, "New designation created", newDesignation);
	}

	public Response getDesignations() {
		List<Designation> designations = designationRepo.findAll();
		return new Response(LocalDateTime.now(), HttpStatus.OK, "All designation fetched", designations);
	}

}
