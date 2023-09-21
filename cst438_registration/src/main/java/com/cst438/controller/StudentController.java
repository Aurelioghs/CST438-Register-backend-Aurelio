package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
@CrossOrigin 
@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    private EnrollmentRepository enrollmentRepository;
//    Get all student
	@GetMapping("/student")
	public StudentDTO[] getAllStudent() {
		
		Iterable<Student> list = studentRepository.findAll();
		ArrayList<StudentDTO> StudentList = new ArrayList<>(); 
		
		for(Student s : list) {
//			StudentDTO d = new StudentDTO(s.getStudent_id(), s.getName(), s.getEmail(), s.getStatusCode(), s.getStatus());
//			StudentList.add(d);
		}
		return StudentList.toArray(new StudentDTO[StudentList.size()]);
	}
	
//	get one student given id
	@GetMapping("/student/{id}")
	public StudentDTO getStudent(@PathVariable("id") int id) {
		
		
		
		return new StudentDTO(s.getStudent_id(), s.getName(), s.getEmail(), s.getStatusCode(), s.getStatus());
	}
	
//	create new student, put to data base
	@PostMapping("/student")
	public int newstudent(@RequestBody StudentDTO studentDTO) {
		
//		check for duplicates
		Student duplicate = studentRepository.findByEmail(studentDTO.email());
		if(duplicate != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already exist");
			
		}
		
		Student myStudent = new Student();
//		Get the data
		myStudent.setEmail(studentDTO.email());
		myStudent.setName(studentDTO.name());
		myStudent.setStatus(studentDTO.status());
		myStudent.setStatusCode(studentDTO.statusCode());
		
		studentRepository.save(myStudent);
		
		return myStudent.getStudent_id();
	}
	
}
//update
//check if id exist, 
//Same as create, check if email already exist, no duplicate email




//delete
@DeleteMapping("/student/{id}")
public void deleteStudent(@PathVariable("id") int id, @RequestParam("force") Optional<String> force){
//	check for id null

//	check for enrollment exist for student
	List<Enrollment> enrollments = enrollmentRepository.findByStudentId(id);

//	Check if null 
	
//	check for improper parameters
	
//	use force.isPresent()
//		force.isEmpty()
	
	
}

//instead of request body (@

 

	
	

