package com.virtusa.empportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.virtusa.empportal.model.LeaveMaster;

public interface LeaveMasterRepo extends JpaRepository<LeaveMaster, Integer> {

	boolean existsByLeaveType(String leaveType);

	@Query("select sum(maxLeaveNumbers) from LeaveMaster")
	int sumOfAllLeaves();

}
