package com.deepak.training_batch_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.deepak.training_batch_management.security.JwtFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers(
            			"/login.html",
            			"/admin.html",
            			"/trainer.html",
            			"/css/**",
            			"/js/**",
            			"/images/**").permitAll()
            	.requestMatchers("/auth/**").permitAll()
            	.requestMatchers("/admin/**").hasRole("ADMIN")
            	.requestMatchers("/trainer/**").hasRole("TRAINER")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}