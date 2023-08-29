package com.virtusa.empportal.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.virtusa.empportal.model.Course;
import com.virtusa.empportal.service.CourseService;
import com.virtusa.empportal.utils.Response;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/course")
public class CourseController {
	
	@Autowired
	CourseService courseService;
	
	@GetMapping
	public Response getAllCourses() {
		return courseService.getCourses();
	}
	
	@PostMapping("/hr/addCourse")
	public Response addCourse(@RequestBody Course course) {
		return courseService.addCourse(course);
	}
	
	@PostMapping("/hr/{id}/addChapter")
	public Response addChapter(@PathVariable String id, @RequestParam String name, @RequestParam String content, @RequestParam @Nullable String videoLink, @RequestParam @Nullable MultipartFile pdf, HttpServletResponse res) {
		try {
			int courseId = Integer.parseInt(id);
		return courseService.addCourseChapter(courseId, name, content, videoLink, pdf);
		} catch(NumberFormatException exception) {
			res.setStatus(400);
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}
	
	@PostMapping("/hr/{id}/assign")
	public Response assignCourse(@PathVariable String id, @RequestParam @Nullable Integer employeeId, @RequestParam @Nullable Integer departmentId, HttpServletResponse res){
		try {
			int courseId = Integer.parseInt(id);
		return courseService.assignCourse(courseId, employeeId, departmentId);
		} catch(NumberFormatException exception) {
			res.setStatus(400);
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not convert "+id+" to integer", null);
		}
	}

}
