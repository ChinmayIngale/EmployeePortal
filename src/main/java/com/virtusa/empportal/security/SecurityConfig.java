package com.virtusa.empportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
//	@Autowired
//	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//	@Autowired
//	JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter,JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception{
	    http.csrf(csrf -> csrf.disable())
	    	.authorizeHttpRequests(request -> request
//	    			.requestMatchers("/jobVacancy/hr/**").hasAuthority("HR")
//					.requestMatchers("/leaveMaster","/leaveMaster/**").hasAuthority("LEAVEMASTER")
//					.requestMatchers("/auth/**","/employee/addEmp","/employee/all").permitAll()
//					.requestMatchers("/**").hasAuthority("EMPLOYEE")
					.requestMatchers("/**").permitAll()
	    			.anyRequest().authenticated())
	    	.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
	    	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	    	
	
	    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	    
	    return http.build();
	}
}
