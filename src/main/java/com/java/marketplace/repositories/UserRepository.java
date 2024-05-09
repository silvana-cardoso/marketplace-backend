package com.java.marketplace.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.marketplace.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
//	identificar se o username foi encontrado ou n no bd
	Optional<User> findByEmail(String email);
}
