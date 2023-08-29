package com.virtusa.empportal.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.virtusa.empportal.utils.ReferanceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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

	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Email
	@Column(nullable = false)
	private String email;
	
	@Lob
	@Column(nullable = false, length = 2097152)
	private byte[] resume;

	@Column(nullable = false)
	private String connection;
	
	@Column(nullable = false)
	private boolean hasJob;
	
	@Column(nullable = false)
	private boolean willingToRelocation;
	
	@Column(nullable = false)
	String skills;
	
	@Enumerated(EnumType.STRING)
	private ReferanceStatus status;
}
