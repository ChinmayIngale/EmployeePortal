package com.virtusa.empportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Chapter {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chapter_ID")
	private int chapterId;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = true)
	private String videoLink;
	
	@Column(nullable = false)
	private String content;
	
	@Transient
	private boolean completed;
	
//	@Lob
//	@Column(nullable = true, length = 2097152)
//	private byte[] pdf;
	private String pdf;

	@ManyToOne
	@JoinColumn(referencedColumnName = "course_ID")
	@JsonIgnore
	private Course course;

	public Chapter(String name, String videoLink, String content, String pdf, Course course) {
		super();
		this.name = name;
		this.videoLink = videoLink;
		this.content = content;
		this.pdf = pdf;
		this.course = course;
	}
	
	
}
