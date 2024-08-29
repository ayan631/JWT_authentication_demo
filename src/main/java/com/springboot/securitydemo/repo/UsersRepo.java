package com.springboot.securitydemo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.securitydemo.model.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {
	public Users findByUsername(String username);
}
