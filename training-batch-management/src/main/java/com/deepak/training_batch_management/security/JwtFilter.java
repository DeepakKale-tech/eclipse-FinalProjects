package com.deepak.training_batch_management.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private CustomerUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain) 
					throws ServletException , IOException
	{
		String authHeader = request.getHeader("Authorization");
		
		if (authHeader!=null && authHeader.startsWith("Bearer "))
		{
			String token = authHeader.substring(7);
			String email = jwtUtil.extractEmail(token);
			
			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			    if (jwtUtil.validateToken(token, userDetails)) {

			        UsernamePasswordAuthenticationToken authToken =
			                new UsernamePasswordAuthenticationToken(
			                        userDetails,
			                        null,
			                        userDetails.getAuthorities());

			        authToken.setDetails(
			                new org.springframework.security.web.authentication.WebAuthenticationDetailsSource()
			                        .buildDetails(request));

			        SecurityContextHolder.getContext().setAuthentication(authToken);
			    }
			}
			
		}
		
		filterChain.doFilter(request, response);
	}
}
