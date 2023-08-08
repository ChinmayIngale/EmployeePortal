package com.virtusa.empportal.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Leaves {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaves_ID")
	private int leavesID;
	
	@OneToMany(mappedBy = "leaves")
	@JsonIgnoreProperties({"leaves"})
	private List<EmployeesLeaves> leavesTaken;
	
	private int totalLeaveTaken;
	
	@Transient
	private int leavesTakenInCurrentYear;
	
	@Transient
	private List<LeaveMaster> leavesRemaining;
	
}
