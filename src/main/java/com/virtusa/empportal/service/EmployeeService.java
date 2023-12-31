package com.virtusa.empportal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.virtusa.empportal.model.Department;
import com.virtusa.empportal.model.Designation;
import com.virtusa.empportal.model.Employees;
import com.virtusa.empportal.model.Messages;
import com.virtusa.empportal.model.Roles;
import com.virtusa.empportal.repository.DepartmentRepo;
import com.virtusa.empportal.repository.DesignationRepo;
import com.virtusa.empportal.repository.EmployeeRepo;
import com.virtusa.empportal.repository.RolesRepo;
import com.virtusa.empportal.security.JwtTokenManager;
import com.virtusa.empportal.utils.Response;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	private DepartmentRepo departmentRepo;
	@Autowired
	private DesignationRepo designationRepo;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtTokenManager jwtTokenManager;
	@Autowired
	RolesRepo rolesRepo;

	public List<Employees> getAllEmp() {
		return employeeRepo.findAll();
	}

	public Response addEmployeeInfo(Employees emp) {
		
		if(employeeRepo.existsByEmail(emp.getEmail())) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST,"Employee already exists", null);
		}
		
		Designation designation = designationRepo.findById(emp.getDesignation().getDesignationID()).orElse(null);
		if(designation == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST,"Designation not exists", null);
		}
		Department dept = departmentRepo.findById(emp.getEmpDepartment().getDepartmentID()).orElse(null);
		if(dept == null || dept.isActive()) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST,"Department not exists", null);
		}
		
		emp.setDesignation(designation);
		emp.setEmpDepartment(dept);
		emp.setPassword(passwordEncoder.encode(emp.getPassword()));
		Employees employee = employeeRepo.save(emp);
		return new Response(LocalDateTime.now(), HttpStatus.CREATED, "Emplyee information added", employee);
		
	}

	public Response getEmpById(int id) {
		Employees emp = employeeRepo.findById(id).orElse(null);
		if(emp != null) {
			Set<Messages> messages = new HashSet<>(); 
			messages.addAll( emp.getRecivedMessages());
			messages.addAll(emp.getEmpDepartment().getRecivedMessages());
			messages.addAll(emp.getDesignation().getRecivedMessages());
			emp.setRecivedMessages(messages);
			return new Response(LocalDateTime.now(), HttpStatus.OK, "Employee found", emp);
		}
		else {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Employee not found", null);
		}
	}

	
	public Response removeEmp(int id) {
		if(!employeeRepo.existsById(id)) {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Employee not found", "ERROR");
		} else {
			employeeRepo.setActiveByEmpID(id, false);
			return new Response(LocalDateTime.now(), HttpStatus.OK, "Employee removed", null);
		}
		
	}

	public Response updateEmployee(Employees employee) {
		Employees emp = employeeRepo.findById(employee.getEmpID()).orElse(null);
		if(emp != null) {
			emp.setPassword(passwordEncoder.encode(emp.getPassword()));
			Employees newEmp = employeeRepo.save(employee);
			return new Response(LocalDateTime.now(), HttpStatus.OK, "Employee information Updated", newEmp);
		}
		else {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Employee not found", null);
		}
	}

	public Response getAllMessages(int id) {
		Employees emp = employeeRepo.findById(id).orElse(null);
		if(emp == null) {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Employee not found", "ERROR");
		} else {
			Set<Messages> messages = new HashSet<>(); 
			messages.addAll( emp.getRecivedMessages());
			messages.addAll(emp.getEmpDepartment().getRecivedMessages());
			messages.addAll(emp.getDesignation().getRecivedMessages());
			return new Response(LocalDateTime.now(), HttpStatus.OK, "Getting employee messages", messages);
		}
		
	}


	public Response loginEmployee(String username, String password) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwtToken = jwtTokenManager.generateJwtToken(authentication);
		return new Response(LocalDateTime.now(), HttpStatus.OK, "Employee logged in and token generated", jwtToken);
	}

	public Response addUserRoles() {
		List<Roles> rolesList = new ArrayList<>();
		rolesList.add(new Roles("EMPLOYEE"));
		rolesList.add(new Roles("HR"));
		rolesList.add(new Roles("LEAVEMASTER"));
		rolesList.add(new Roles("ADMIN"));
		List<Roles> roles = rolesRepo.saveAll(rolesList);
		return new Response(LocalDateTime.now(), HttpStatus.OK, "Roles added", roles);
	}

}
