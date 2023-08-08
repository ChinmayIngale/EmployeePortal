package com.virtusa.empportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.empportal.model.Department;

public interface DepartmentRepo extends JpaRepository<Department, Integer>{

	boolean existsByDeptName(String deptName);

}
