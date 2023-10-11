package com.cst438.service;


import java.io.IOException;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@ConditionalOnProperty(prefix = "gradebook", name = "service", havingValue = "mq")
public class GradebookServiceMQ implements GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	Queue gradebookQueue = new Queue("gradebook-queue", true);
	


	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
	    System.out.println("Start Message " + student_email + " " + course_id);
	 // create EnrollmentDTO, convert to JSON string and send to gradebookQueue
	 		// TODO
	    try {
	        EnrollmentDTO enrollment = new EnrollmentDTO(student_email, student_name, course_id);
	        rabbitTemplate.convertAndSend(gradebookQueue.getName(), enrollment);
	        System.out.println("Message sent to gradebook " + student_email + " " + course_id);
	    } catch (AmqpException e) {
	        System.err.println("Error sending enrollment message: " + e.getMessage());
	    }
	}

	
	@RabbitListener(queues = "registration-queue")
	@Transactional
	public void receive(String message) {
	    System.out.println("Receive grades: " + message);
	    
	 
	    FinalGradeDTO[] finalGradeDTO = null;
	    
	    // Loop through the grades and update student grades
	    for (FinalGradeDTO grade : finalGradeDTO) {
	        Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(grade.studentEmail(), grade.courseId());
	        if (enrollment != null) {
	            enrollment.setCourseGrade(grade.grade());
	            enrollmentRepository.save(enrollment);
	            System.out.println("Student Email: " + grade.studentEmail() + " Course ID: " + grade.courseId() + " Grade: " + grade.grade());
	        } else {
	            System.out.println("Enrollment not found: " + grade.studentEmail() + " Course ID: " + grade.courseId());
	        }
	    }
	}

	
	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
