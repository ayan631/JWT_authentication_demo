package com.springboot.securitydemo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.securitydemo.model.Student;
import com.springboot.securitydemo.model.Users;
import com.springboot.securitydemo.repo.UsersRepo;
import com.springboot.securitydemo.service.StudentService;
import com.springboot.securitydemo.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class StudentController {
	
	@Autowired
	private StudentService stdService;
	
	@Autowired
	private UsersService usersService;
	
	@GetMapping("/")
	public String greet(HttpServletRequest request) {
		return "Welome to Ayan's Programming Lab !!"+
				"Your Session ID = "+request.getSession().getId();
		
	}
	
	@GetMapping("/students")
	public List<Student> getStudents() throws RuntimeException{	
		if(this.stdService.showAll()==null)
			throw new RuntimeException("Student List is empty");
		return this.stdService.showAll();
	}
	
	
	@PostMapping("/students")
	public String addNewStudent(@RequestBody Student newStudent) {
		return this.stdService.addStudent(newStudent);
	}
	
	
	@DeleteMapping("/students/{id}")
	public String deleteStudentById(@PathVariable int id) {
		return this.stdService.deleteStudent(id);
	}
	
	
	@PostMapping("/register")
	public Users register(@RequestBody Users newUser) {
		return this.usersService.registerUser(newUser);
	}
	
	
	@PostMapping("/login")
	public String login(@RequestBody Users newUser) {
		return this.usersService.userLogin(newUser);
	}
	
	@GetMapping("/csrf-token")
	public CsrfToken getToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}
	
}
