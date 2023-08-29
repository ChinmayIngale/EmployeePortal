package com.virtusa.empportal.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.virtusa.empportal.model.FileStorage;
import com.virtusa.empportal.repository.FileStorageRepo;
import com.virtusa.empportal.utils.Response;

@Service
public class FileService {
	@Autowired
	FileStorageRepo fileStorageRepo;

	public Object getFile(int fileId) {
		FileStorage file = fileStorageRepo.findById(fileId).orElse(null);
		if(file == null) {
			return new Response(LocalDateTime.now(), HttpStatus.NOT_FOUND, "File with id "+fileId+" not found", null);
		}
		return file;
	}
}
