package com.virtusa.empportal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Course {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_ID")
	private int courseId;
	
	@Column(nullable = false, unique = true)
	private String title;
	
	@Column(nullable = false)
	private String description;
	
	@OneToMany(mappedBy = "course")
	@Column(nullable = false)
	@JsonIgnoreProperties({"course"})
	private List<Chapter> chapters;
	
	@Column(nullable = false)
	private int numberOfChapters;
	
	private boolean active;
}
