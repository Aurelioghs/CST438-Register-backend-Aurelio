package com.cst438.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <User, Integer> {
	
	 User findByUsername(String username);

	 

}
