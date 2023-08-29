package com.virtusa.empportal.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Department {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_ID")
	private int departmentID;
	
	@Column(nullable = false)
	@NotBlank(message = "Department name can not be blank")
	private String deptName;
	
	@ManyToOne
	@JoinColumn(nullable = false, referencedColumnName = "address_ID")
	private Address baseLocation;
	
	private LocalDate dateOfCreation;
	
	@OneToMany(mappedBy = "empDepartment")
//	@JsonIgnoreProperties({"empDepartment","sentMessages","recivedMessages"})
	@JsonIncludeProperties({"empID","email","username"})
	private List<Employees> employees;
	
	@Column(nullable = false)
	private boolean active; 
    
    @ManyToMany(mappedBy = "deliverToDepartments")
    private List<Messages> recivedMessages;
}
