package com.cst438.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;

@Service
@ConditionalOnProperty(prefix = "gradebook", name = "service", havingValue = "rest")
@RestController
public class GradebookServiceREST implements GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	private static String gradebook_url;
	

	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		System.out.println("Start Message "+ student_email +" " + course_id); 
	
		// TODO use RestTemplate to send message to grade book service
		
		EnrollmentDTO enrollment = new EnrollmentDTO(0,student_email, student_name, course_id);
		
//		enrollment.courseId = course_id;
//		enrollment.studentEmail = student_email;
//		enrollment.studentName = student_name;
		
		System.out.println("Post to gradebook "+enrollment);
		EnrollmentDTO response = restTemplate.postForObject (gradebook_url+"/enrollment", enrollment, EnrollmentDTO.class);
		System.out.println("Gradebook Response: " + response);
	}
		

	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	/*
	 * end point for final course grades
	 */
	@PutMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades( @RequestBody FinalGradeDTO[] grades, @PathVariable("course_id") int course_id) {
		System.out.println("Grades received "+grades.length);
		
		//TODO update grades in enrollment records with grades received from grade book service
		
		for (FinalGradeDTO grade : grades) {
            String studentEmail = grade.studentEmail();
            String gradeValue = grade.grade();

            // Find the enrollment for the student in the specified course
         
            Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(studentEmail,course_id);

            if (enrollment != null) {
                // Update the course grade for the enrollment
                enrollment.setCourseGrade(gradeValue);
                enrollmentRepository.save(enrollment);
            } else {
                System.out.println("Enrollment not found for Student Email: " + studentEmail + " Course ID: " + course_id);
                
            }
        }
		
	}
}
