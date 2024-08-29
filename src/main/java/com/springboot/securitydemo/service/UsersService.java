package com.springboot.securitydemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.securitydemo.model.Users;
import com.springboot.securitydemo.repo.UsersRepo;

@Service
public class UsersService {
	
	@Autowired
	private UsersRepo userRepo;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authManager;
	
	public Users registerUser(Users newUser) {
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		return this.userRepo.save(newUser);
	}

	public String userLogin(Users newUser) {
		
		/* 1. Pass username and password to UnsernamePasswordAuthenticationToken() constructor.
		 * 2. It will return an Authenticate type object.
		 * 3. Pass this object to authenticate() method.
		 * 4. It will return you Authentication object.
		 * 5. check whether it is authenticated or not and send return statement.  
		 */
		
		Authentication authentication = 
				this.authManager
				.authenticate(new UsernamePasswordAuthenticationToken(newUser.getUsername(), newUser.getPassword()));
		
		if(authentication.isAuthenticated()) {
			return "JWT Token : [\""+this.jwtService.generateToken(newUser.getUsername())+"\"]";
		}
		else {
			return "Invalid username or password !!";
		}
	}
}
