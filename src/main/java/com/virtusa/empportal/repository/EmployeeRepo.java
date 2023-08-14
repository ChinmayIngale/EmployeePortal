package com.virtusa.empportal.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.virtusa.empportal.model.Employees;
import com.virtusa.empportal.model.Leaves;

import jakarta.transaction.Transactional;

public interface EmployeeRepo extends JpaRepository<Employees, Integer>{

	boolean existsByEmail(String personalEmail);

	@Modifying
	@Transactional
	@Query("update Employees emp set emp.active = :status, emp.terminationDate=CURRENT_DATE() where emp.empID =:id")
	void setActiveByEmpID(int id, boolean status);

	Optional<Employees> findByLeaves(Leaves leaves);

	Optional<Employees> findByEmail(String email);



}
