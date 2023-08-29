package com.virtusa.empportal.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Messages {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_ID")
    private int messageID;
	
	@Column(nullable = false)
	@NotBlank(message = "Message title can not be blank")
	private String messageTitle;
	
	@Column(nullable = false)
	private String messageBody;
	
	@Column(nullable = false)
	private LocalDateTime timeOfCreation = LocalDateTime.now();
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "emp_ID", nullable = false)
	@JsonIncludeProperties({"empID","email","username"})
	private Employees sender;
	
	@ManyToMany
	@JoinTable(name = "Employee_Messages",
				joinColumns = {@JoinColumn(name = "message_ID")},
				inverseJoinColumns = {@JoinColumn(name = "emp_ID")})
//	@JsonIgnoreProperties({"sentMessages","recivedMessages"})
	@JsonIncludeProperties({"empID","email","username"})
	private Set<Employees> deliverToEmployees;
	
	@ManyToMany
	@JoinTable(name = "Deparment_Messages",
				joinColumns = {@JoinColumn(name = "message_ID")},
				inverseJoinColumns = {@JoinColumn(name = "department_ID")})
//	@JsonIgnoreProperties({"recivedMessages"})
	@JsonIncludeProperties({"departmentID", "deptName"})
	private Set<Department> deliverToDepartments;
	
	@ManyToMany
	@JoinTable(name = "Designation_Messages",
				joinColumns = {@JoinColumn(name = "message_ID")},
				inverseJoinColumns = {@JoinColumn(name = "designation_ID")})
//	@JsonIgnoreProperties({"recivedMessages"})
	@JsonIncludeProperties({"designationID", "designationName"})
	private Set<Designation> deliverToDesignations;
	
}
