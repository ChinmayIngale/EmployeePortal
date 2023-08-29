package com.virtusa.empportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.empportal.model.Course;

public interface CourseRepo extends JpaRepository<Course, Integer>{

	Optional<Course> findByTitle(String title);

}
