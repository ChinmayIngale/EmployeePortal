package com.virtusa.empportal.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Embeddable
public class EmployeeCourseId implements Serializable {
	
	@Transient
	private static final long serialVersionUID = 1L;
	int courseId;
	int employeeId;
}
