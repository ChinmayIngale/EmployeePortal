package com.virtusa.empportal.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class JobVacancy {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_ID")
	private int jobID;
	
	private String title;
	
	@ManyToOne
    @JoinColumn(referencedColumnName = "designation_ID", nullable = false)
    @JsonIncludeProperties({"designationID","designationName"})
	private Designation position;
	private int experianceInYears;
	
	@ElementCollection
	private Set<String> location;
	@ElementCollection
	private List<String> requirement;
	
	private LocalDate createdOn;
	
	private boolean fulfilled;
	
	@OneToMany(mappedBy = "job")
	@JsonIgnoreProperties({"resume"})
	private List<Reference> references;
}
