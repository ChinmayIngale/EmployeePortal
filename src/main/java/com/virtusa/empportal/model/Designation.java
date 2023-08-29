package com.virtusa.empportal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Designation {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "designation_ID")
    private int designationID;
	
	@Column(nullable = false)
	@NotBlank(message = "Dessignation name can not be blank")
	private String designationName;
	
	@OneToMany(mappedBy = "designation")
//	@JsonIgnoreProperties({"designation","sentMessages","recivedMessages"})
	@JsonIncludeProperties({"empID","email","username"})
	private List<Employees> employees;
    
    @ManyToMany(mappedBy = "deliverToDesignations")
    private List<Messages> recivedMessages;
	
}
