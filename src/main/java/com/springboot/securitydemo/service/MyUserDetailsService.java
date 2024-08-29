package com.springboot.securitydemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.securitydemo.model.UserPrincipal;
import com.springboot.securitydemo.model.Users;
import com.springboot.securitydemo.repo.UsersRepo;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UsersRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Users user = userRepo.findByUsername(username);
	    if (user == null) {
	        System.out.println("User not found with name = " + username);
	        throw new UsernameNotFoundException("User not found with name = " + username);
	    }
	    System.out.println("User found: " + user.getUsername());
	    return new UserPrincipal(user);
	}

}
