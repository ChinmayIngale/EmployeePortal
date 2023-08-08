package com.virtusa.empportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.empportal.model.Designation;

public interface DesignationRepo extends JpaRepository<Designation, Integer>{

	boolean existsByDesignationName(String designationName);

}
