package com.virtusa.empportal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.virtusa.empportal.model.Address;
import com.virtusa.empportal.model.Department;
import com.virtusa.empportal.repository.AddressRepo;
import com.virtusa.empportal.repository.DepartmentRepo;
import com.virtusa.empportal.utils.Response;

@Service
public class DepartmentService {
	
	@Autowired
	DepartmentRepo departmentRepo;
	
	@Autowired
	AddressRepo addressRepo;

	
	public Response addDepartment(Department department) {
		if(departmentRepo.existsByDeptName(department.getDeptName())) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST,"Departemt with same name exists", null);
		}
		Address address = addressRepo.save(department.getBaseLocation());
		department.setBaseLocation(address);
		
		Department dept = departmentRepo.save(department);
		return new Response(LocalDateTime.now(), HttpStatus.CREATED, "New department created", dept);
	}

	public Response getAllDepartments() {
		List<Department> departments = departmentRepo.findAll();
		return new Response(LocalDateTime.now(), HttpStatus.OK, "All Departments fetched", departments);
	}

	public Response softDeleteDepartment(int id) {
		Department dept = departmentRepo.findById(id).orElse(null);
		if(dept == null) {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND,"Department with id "+id+" not found", "ERROR");
		} else if(!dept.isActive()){
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, dept.getDeptName()+" Department already deleted", null);
		} else if(!dept.getEmployees().isEmpty()){
			return new Response(LocalDateTime.now(), HttpStatus.CONFLICT, dept.getDeptName()+" department still has employees", null);
		} else {
			dept.setActive(false);
			departmentRepo.save(dept);
			return new Response(LocalDateTime.now(), HttpStatus.OK, dept.getDeptName()+" Department removed", null);
		}
	}

}
