package com.cst438;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.domain.StudentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class JunitTestStudent{
	@Autowired
	private MockMvc mvc;
	
//	Run one for post and one for get
	  @Test
	  public void addStudent() throws Exception {
//		  Create new student DTO object 
		  StudentDTO s = new StudentDTO(0, "name test", "alopezmartinez@csumb.edu", 0, null);
	
		  MockHttpServletResponse response;

		  response = mvc.perform(
				MockMvcRequestBuilders
			    	.post("/student/create")
			    	.contentType(MediaType.APPLICATION_JSON)
			    	.content(asJsonString(s))
			    	.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());
		
		// verify that returned data has non zero primary key
		
//		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		
		int student_id = Integer.parseInt(response.getContentAsString());
		assertNotEquals(0,student_id);
		
//		retrieve student 
		response = mvc.perform(
				MockMvcRequestBuilders
			      .get("/student/" + student_id)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());

		StudentDTO st = fromJsonString(response.getContentAsString(), StudentDTO.class);
		
		assertEquals(s.name(), st.name());
		assertEquals(s.email(), st.email());
		assertEquals(s.statusCode(), st.statusCode());
	}
	  
	  
//	 test get all student
	  @Test
	  void testGetAllAssignment() throws Exception {
	        // Get all assignments - assert status 200
	        mvc.perform(MockMvcRequestBuilders.get("/student"))
	                .andExpect(status().isOk())
	                .andReturn().getResponse();
	    }
	 
	  
//	  Update JunitTest Code- 3 test
//	  @Test  updateStudent
//	  public void deleteStudent()
//	  1. checking duplicate email
//	  2. checking non-exising student
//	  3. normal test-success test
	  
//	  update Student
	  @Test
	  void updateStudent() throws Exception {
	      // Student ID of an existing student 
	      int studentIdToUpdate = 007; 

	      StudentDTO updatedStudentDTO = new StudentDTO(studentIdToUpdate, "Aureli Lopez", "test@csumb.com", 0, null);

	      MockHttpServletResponse response = mvc.perform(
	              MockMvcRequestBuilders.put("/student/update/{id}", studentIdToUpdate)
	                      .contentType(MediaType.APPLICATION_JSON)
	                      .content(asJsonString(updatedStudentDTO))
	                      .accept(MediaType.APPLICATION_JSON))
	              .andReturn().getResponse();

	      assertEquals(200, response.getStatus());

	      // Retrieve the updated student
	      response = mvc.perform(
	              MockMvcRequestBuilders.get("/student/" + studentIdToUpdate)
	                      .accept(MediaType.APPLICATION_JSON))
	              .andReturn().getResponse();

	      assertEquals(200, response.getStatus());
	      StudentDTO updatedStudent = fromJsonString(response.getContentAsString(), StudentDTO.class);
	      assertEquals(updatedStudentDTO.name(), updatedStudent.name());
	      assertEquals(updatedStudentDTO.email(), updatedStudent.email());
	      assertEquals(updatedStudentDTO.statusCode(), updatedStudent.statusCode());
	  }


	   
//		Delete- 2 test
	  @Test
	  void testDeleteStudent() throws Exception {
	      // Delete a student
	      int studentIdToDelete = 007; 	  
	      mvc.perform(MockMvcRequestBuilders.delete("/deleteStudent/{id}", studentIdToDelete))
	      .andExpect(status().isOk());

	      // delete the same student again 
	      mvc.perform(MockMvcRequestBuilders.delete("/deleteStudent/{id}", studentIdToDelete))
	              .andExpect(status().isNotFound());

	      //delete a non-existent student
	      int nonExistentStudentId = 9; // Replace with a non-existent student ID
	      mvc.perform(MockMvcRequestBuilders.delete("/deleteStudent/{id}", nonExistentStudentId))
	              .andExpect(status().isNotFound());
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
