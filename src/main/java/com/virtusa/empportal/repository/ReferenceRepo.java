package com.virtusa.empportal.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.empportal.model.Employees;
import com.virtusa.empportal.model.Reference;

public interface ReferenceRepo extends JpaRepository<Reference, Integer> {

	Set<Reference> findAllByEmployee(Employees employee);

}
