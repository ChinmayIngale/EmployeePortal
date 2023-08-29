package com.virtusa.empportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class FileStorage {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_ID")
	private int fileId;
	
	@Lob
	@Column(nullable = true, length = 2097152)
	private byte[] file;
	
	private String name;
	private String fileType;
	
	
	public FileStorage(byte[] file, String name, String fileType) {
		super();
		this.file = file;
		this.name = name;
		this.fileType = fileType;
	}
	

	
}
