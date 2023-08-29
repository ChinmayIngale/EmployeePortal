package com.virtusa.empportal.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "employee_course")
public class EmployeeCourse {

	@EmbeddedId
	EmployeeCourseId id = new EmployeeCourseId();	
	
	@ManyToOne
	@MapsId("courseId")
	@JoinColumn(referencedColumnName = "course_ID")
	private Course course;
	
	@ManyToOne
	@MapsId("employeeId")
	@JoinColumn(referencedColumnName = "emp_ID")
	@JsonIgnore
	private Employees employee;
	
	private int totalNumberOfChapters;
	private int NumberOfCompletedChapters;
	
	@OneToMany
	private Set<Chapter> completedChapters;

	public EmployeeCourse(Course course, Employees employee) {
		super();
		this.course = course;
		this.employee = employee;
		this.totalNumberOfChapters = course.getNumberOfChapters();
		this.NumberOfCompletedChapters = 0;
	}

}
