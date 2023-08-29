package com.virtusa.empportal.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.virtusa.empportal.model.Chapter;
import com.virtusa.empportal.model.Course;
import com.virtusa.empportal.model.Department;
import com.virtusa.empportal.model.EmployeeCourse;
import com.virtusa.empportal.model.EmployeeCourseId;
import com.virtusa.empportal.model.Employees;
import com.virtusa.empportal.model.FileStorage;
import com.virtusa.empportal.repository.ChapterRepo;
import com.virtusa.empportal.repository.CourseRepo;
import com.virtusa.empportal.repository.DepartmentRepo;
import com.virtusa.empportal.repository.EmployeeCourseRepo;
import com.virtusa.empportal.repository.EmployeeRepo;
import com.virtusa.empportal.repository.FileStorageRepo;
import com.virtusa.empportal.utils.Response;


@Service
public class CourseService {
	
	@Autowired
	CourseRepo courseRepo;
	
	@Autowired
	ChapterRepo chapterRepo;
	@Autowired
	EmployeeRepo employeeRepo;
	@Autowired
	DepartmentRepo departmentRepo;
	@Autowired
	EmployeeCourseRepo employeeCourseRepo;
	@Autowired
	FileStorageRepo fileStorageRepo;
	

	public Response addCourse(Course course) {
		Course existCourse = courseRepo.findByTitle(course.getTitle()).orElse(null);
		if(existCourse != null) {
			return new Response(LocalDateTime.now(), HttpStatus.CONFLICT, "COurse woth title '"+course.getTitle()+"' already exists", existCourse);
		}
		Course newCourse = courseRepo.save(course);
		return new Response(LocalDateTime.now(), HttpStatus.OK, "Course created", newCourse);
	}

	public Response addCourseChapter(int courseId, String name, String content, String videoLink, MultipartFile pdf) {
		Course course = courseRepo.findById(courseId).orElse(null);
		if(course == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "COurse woth id: "+courseId+" does not exists", null);
		}
		try {
			String url= null;
			if(pdf != null) {
			FileStorage file = fileStorageRepo.save(new FileStorage(pdf.getBytes(), pdf.getOriginalFilename(), pdf.getContentType()));
			url = "http://localhost:8080"+"/auth/filestorage/"+file.getFileId();
			}
			
			chapterRepo.save(new Chapter(name, videoLink, content, url, course));

			course.setNumberOfChapters(course.getNumberOfChapters()+1);
			Course newCourse = courseRepo.save(course);
			
			return new Response(LocalDateTime.now(), HttpStatus.OK, "Chapter added course", newCourse);
		} catch (IOException e) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can not save resume", e.getMessage());
		}
	}

	public Response getCourses() {
		List<Course> courses = courseRepo.findAll();
		return new Response(LocalDateTime.now(), HttpStatus.OK, "course fetched", courses);
	}

	public Response assignCourse(int courseId, Integer employeeId, Integer departmentId) {
		Course course = courseRepo.findById(courseId).orElse(null);
		if(course == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Course with id: "+courseId+" does not exists", null);
		}
		
		Set<Employees> employees = new HashSet<>();
		if(employeeId != null) {
			Employees emp = employeeRepo.findById(employeeId).orElse(null);
			if (emp == null) {
				return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "employee not found", null);
			}
			employees.add(emp);
		}
		
		if(departmentId != null) {
			Department dept= departmentRepo.findById(departmentId).orElse(null);
			if(dept == null) {
				return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST," Department with id: "+departmentId+" does not exist.", null);
			} else {
				dept.getEmployees().forEach(emp -> {
					employees.add(emp);
				});
			}
		}
		
		for(Employees emp : employees) {
			employeeCourseRepo.save(new EmployeeCourse(course, emp));
		}
			
		return new Response(LocalDateTime.now(), HttpStatus.OK, "course assigned", null);
	}

	public Response getAssignedCourses(int employeeId) {
		Employees emp = employeeRepo.findById(employeeId).orElse(null);
		if (emp == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "employee not found", null);
		}
		Set<EmployeeCourse> courses = employeeCourseRepo.findAllByEmployee(emp);
		Set<Map<String, Object>> result = new HashSet<>();
		for(EmployeeCourse empCourse: courses) {
			List<Chapter> courseChapters = empCourse.getCourse().getChapters();
			
			for(int i=0; i < courseChapters.size(); i++){
				Chapter chapter = courseChapters.get(i);
				if(empCourse.getCompletedChapters().contains(chapter)) {
				chapter.setCompleted(true);
				courseChapters.set(i, chapter);
				}
			}
			empCourse.getCourse().setChapters(courseChapters);
			
			Map<String, Object> course= ConvertToMap(empCourse.getCourse());
			course.put("NumberOfCompletedChapters", empCourse.getNumberOfCompletedChapters());
			result.add(course);
		}
		return new Response(LocalDateTime.now(), HttpStatus.OK, "Assigned courses fetched", result);
	}
	
	public static Map<String, Object> ConvertToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try { map.put(field.getName(), field.get(obj)); } catch (Exception e) { }
        }
        return map;
    }
	
	public Response setChapterStatus(int employeeId, int courseId, int chapterId) {
		Employees emp = employeeRepo.findById(employeeId).orElse(null);
		if (emp == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "employee not found", null);
		}
		
		Course course = courseRepo.findById(courseId).orElse(null);
		if(course == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Course with id: "+courseId+" does not exists", null);
		}
		
		Chapter chapter = course.getChapters().stream().filter(ch -> ch.getChapterId() == chapterId).findFirst().orElse(null);
		if(chapter == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Course does not have chapter with id: "+chapterId, null);
		}
		
		EmployeeCourse employeeCourse = employeeCourseRepo.findById(new EmployeeCourseId(courseId, employeeId)).orElse(null);
		if(employeeCourse == null) {
			return new Response(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Employee is not assigned to given course", null);			
		}
		
		employeeCourse.setNumberOfCompletedChapters(employeeCourse.getNumberOfCompletedChapters()+1);
		Set<Chapter> chapters = employeeCourse.getCompletedChapters();
		chapters.add(chapter);
		employeeCourse.setCompletedChapters(chapters);
		
		employeeCourseRepo.save(employeeCourse);

		return new Response(LocalDateTime.now(), HttpStatus.OK, "Chapter status changes to completed", null);
	}

}


