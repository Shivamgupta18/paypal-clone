package com.service.util;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTrequestFilter extends OncePerRequestFilter {

	private JwtUtil jwtutil;

	public JWTrequestFilter(JwtUtil jwtutil) {
		// super();
		this.jwtutil = jwtutil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String authorizationheader = request.getHeader("Authorization");

		String username = null;
		String jwt = null;
		if (authorizationheader != null && authorizationheader.startsWith("Bearer")) {
			jwt = authorizationheader.substring(7);
			try {
				username = jwtutil.extractUsername(jwt);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtutil.validateToken(jwt, username)) {
				UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(username, null,
						null);
				authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authtoken);
			}
		}

		if (authorizationheader != null && authorizationheader.startsWith("Bearer")) {
			jwt = authorizationheader.substring(7);
			if (jwt == null || jwt.isBlank()) {
				chain.doFilter(request, response);
				return;
			}
			try {
				username = jwtutil.extractUsername(jwt);
				String role = jwtutil.extractRole(jwt);

				UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(username, null,
						List.of(new SimpleGrantedAuthority(role)));
				authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authtoken);
				chain.doFilter(request, response);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else {
			chain.doFilter(request, response);
			return;
		}
	}

}
