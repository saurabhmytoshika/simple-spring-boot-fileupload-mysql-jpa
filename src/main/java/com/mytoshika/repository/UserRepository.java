package com.mytoshika.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytoshika.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
}
