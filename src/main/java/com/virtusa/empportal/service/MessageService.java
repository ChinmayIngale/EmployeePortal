package com.virtusa.empportal.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.virtusa.empportal.model.Department;
import com.virtusa.empportal.model.Designation;
import com.virtusa.empportal.model.Employees;
import com.virtusa.empportal.model.Messages;
import com.virtusa.empportal.repository.DepartmentRepo;
import com.virtusa.empportal.repository.DesignationRepo;
import com.virtusa.empportal.repository.EmployeeRepo;
import com.virtusa.empportal.repository.MessagesRepo;
import com.virtusa.empportal.utils.Response;

@Service
public class MessageService {

	@Autowired
	private MessagesRepo messagesRepo;
	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	private DepartmentRepo departmentRepo;
	@Autowired
	private DesignationRepo designationRepo;

	public Response sendMessage(Messages msg) {
		// check sender
		Employees sender = employeeRepo.findById(msg.getSender().getEmpID()).orElse(null);
		if (sender == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Sender employee not found", null);
		}
		msg.setSender(sender);
		
		Set<Employees> receivingEmp = new HashSet<>();
		Set<Department> receivingDept = new HashSet<>();
		Set<Designation> receivingDes = new HashSet<>();

		// check receiving employees
		if (msg.getDeliverToEmployees() != null) {
			for (Employees emp : msg.getDeliverToEmployees()) {
				Employees employee = employeeRepo.findById(emp.getEmpID()).orElse(null);
				if (employee == null) {
					return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Employee "+emp.getEmail()+" not found",null);
				} else {
					receivingEmp.add(employee);
				}
			}
		}
		
		//check Department
		if(msg.getDeliverToDepartments() != null) {
		for(Department department: msg.getDeliverToDepartments()) {
			Department dept= departmentRepo.findById(department.getDepartmentID()).orElse(null);
			if(dept == null) {
				return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, department.getDeptName()+" Department does not exist.", null);
			} else {
				receivingDept.add(dept);
			}
		}}
		
		//check Designation
		if(msg.getDeliverToDesignations() != null) {
		for(Designation designation: msg.getDeliverToDesignations()) {
			Designation des= designationRepo.findById(designation.getDesignationID()).orElse(null);
			if(des == null) {
				return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, designation.getDesignationName()+" Designation does not exist.", null);
			} else {
				receivingDes.add(des);
			}
		}}
		
		msg.setDeliverToEmployees(receivingEmp);
		msg.setDeliverToDepartments(receivingDept);
		msg.setDeliverToDesignations(receivingDes);
		Messages sentMsg = messagesRepo.save(msg);
		return new Response(LocalDateTime.now(), HttpStatus.CREATED, "Message sent.", sentMsg);
	}

}
