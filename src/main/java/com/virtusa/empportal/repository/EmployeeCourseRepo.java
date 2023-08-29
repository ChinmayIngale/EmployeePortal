package com.virtusa.empportal.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.empportal.model.EmployeeCourse;
import com.virtusa.empportal.model.EmployeeCourseId;
import com.virtusa.empportal.model.Employees;

public interface EmployeeCourseRepo extends JpaRepository<EmployeeCourse, EmployeeCourseId> {

	Set<EmployeeCourse> findAllByEmployee(Employees emp);

}
