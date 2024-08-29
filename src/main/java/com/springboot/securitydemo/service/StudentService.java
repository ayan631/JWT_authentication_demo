package com.springboot.securitydemo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.securitydemo.model.Student;

@Service
public class StudentService {

	private List<Student> students = new ArrayList<>();
	
	public StudentService() {
		this.students.add(new Student(1001, "Ayan", 85.75));
		this.students.add(new Student(1002, "Aaiswaryya", 88.50));
		this.students.add(new Student(1003, "Lalita", 81.25));
	}
	
	public String addStudent(Student std) {
		try {
			this.students.add(std);
		}catch(Exception ex) {
			return ex.getMessage();
		}
		return "Student with id : "+std.getId()+" is Added successfully !!";
	}
	
	public List<Student> showAll(){
		if(students.isEmpty())
			return null;
		
		return this.students;
	}
	
	public String deleteStudent(int id) {
		try {
			Student st = this.students.stream().filter(std-> std.getId()==id).findAny().get();
			students.remove(st);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return "Oops!! Something went wrong!";
		}
		return "Successfully deleted !!";
	}
}
