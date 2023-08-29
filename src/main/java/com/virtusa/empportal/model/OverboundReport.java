package com.virtusa.empportal.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OverboundReport {
	
	@JsonIncludeProperties({"empID","email","username"})
	Employees employee;
	int leavesTaken;
	int leavesRemaining;
	int overboundLeaves;

}
