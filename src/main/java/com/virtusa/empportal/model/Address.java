package com.virtusa.empportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Address {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_ID")
    private int addressID;
	
	@Column(nullable = false)
	private int pincode;
	
	@Column(nullable = false)
    @NotBlank(message = "State cannot be blank")
	private String state;
	
	@Column(nullable = false)
    @NotBlank(message = "City cannot be blank")
	private String city;
	
	@Column(nullable = false)
    @NotBlank(message = "Address cannot be blank")
	private String completeAddress;
	
	@Column(nullable = false)
	private boolean currentAddress;
	
}
