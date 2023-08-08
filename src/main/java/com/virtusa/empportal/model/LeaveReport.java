package com.virtusa.empportal.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaveReport {

	@JsonIncludeProperties({"empID","email","username"})
	private final Employees emp;
	private final Set<EmployeesLeaves> employeesLeaves;
}
