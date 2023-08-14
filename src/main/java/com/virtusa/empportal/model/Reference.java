package com.virtusa.empportal.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Entity
public class Reference {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reference_ID")
	private int referenceID;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "emp_ID", nullable = false)
	@JsonIncludeProperties({"empID", "email"})
	private Employees employee;

	@ManyToOne
	@JoinColumn(referencedColumnName = "job_ID", nullable = false)
	@JsonIncludeProperties({"jobID", "title"})
	private JobVacancy job;

	private String firstName;
	private String lastName;
	
	@Email
	private String email;
}
