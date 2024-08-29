package com.springboot.securitydemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.securitydemo.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtFilter jwtFilter;

	/* Declare a PasswordEncoder Bean for encoding user-password */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * We need to create a bean of SecurityFilterChain to define custom security
	 * filters. As soon as you declare the bean, the default form-based
	 * authentication will be disabled. By doing so, We've actually byparse the
	 * form-based authentication.
	 */

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(customizer -> customizer.disable()); // csrf is disabled
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("login", "register")
				.permitAll()
				.anyRequest()
				.authenticated()
			);

		// http.formLogin(Customizer.withDefaults());
		http.httpBasic(Customizer.withDefaults()); // for authenticating via Postman/other applications

		// make the session state-less
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		//add jwtFilter before  filter
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build(); // build the SecurityFilterChain
	}

	/*	Create our own AuthenticationProvider bean instead of using Default
	 	AuthenticationProvider by Spring Security. 
	 	Use it when you are using Basic Authorization for user authentication.	*/

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		return daoAuthenticationProvider;
	}

	
	
	/*	Create a Bean of AuthenticationManager class along with AuthenticationProvider bean when you are using 
	 	JWT Authentication technique for user authentication.	*/
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	// create a bean of UserDetailsService to set custom username password and role for authentication
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user1 = User.builder()
//				.username("Ayan")
//				.password(passwordEncoder().encode("a@123"))
//				.roles("USER")
//				.build();
//		
//		UserDetails user2 = User.builder()
//				.username("Rai")
//				.password(passwordEncoder().encode("r@123"))
//				.roles("ADMIN")
//				.build();
//		
//		//just to print and see the BCrypt passwords
//		System.out.println("Password of user1 : "+user1.getPassword().toString());
//		System.out.println("Password of user2 : "+user2.getPassword().toString());
//		
//		return new InMemoryUserDetailsManager(user1, user2);
//	}

}
