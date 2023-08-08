package com.virtusa.empportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.empportal.model.EmployeesLeaves;
import com.virtusa.empportal.model.LeaveMaster;

public interface EmployeesLeavesRepo extends JpaRepository<EmployeesLeaves, Integer> {

	List<EmployeesLeaves> findAllByLeaveMaster(LeaveMaster leaveMaster);

}
