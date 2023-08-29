package com.virtusa.empportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.empportal.model.FileStorage;

public interface FileStorageRepo extends JpaRepository<FileStorage, Integer> {

}
