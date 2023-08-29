package com.virtusa.empportal.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class EmployeesLeaves {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employeesleaves_ID")
	private int employeesleavesID;

	@ManyToOne
	@JoinColumn(referencedColumnName = "leavemaster_ID")
	@JsonIncludeProperties({"leavemasterID", "leaveType"})
	private LeaveMaster leaveMaster;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "leaves_ID")
	@JsonIgnore
	private Leaves leaves;
	
	private LocalDate fromDate;
	private LocalDate toDate;
	private int days;
	
	private String comment;
	
}
