package com.virtusa.empportal.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.empportal.model.Employees;
import com.virtusa.empportal.model.JobVacancy;
import com.virtusa.empportal.model.Reference;

public interface JobVacancyRepo extends JpaRepository<JobVacancy, Integer> {

	Optional<JobVacancy> findByTitleAndFulfilled(String title, boolean fulfilled);

	Optional<JobVacancy> findByJobIDAndFulfilled(int jobID, boolean fulfilled);

}
