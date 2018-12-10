package com.example.AuthService.security;

import java.io.IOException;
import java.security.Signature;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private final JwtConfig jwtConfig;

	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
			JwtConfig jwtConfig) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtConfig = jwtConfig;
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
//		throws AuthenticationException {

		try {
			UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), Collections.emptyList());
			return authenticationManager.authenticate(authToken);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		Long now = System.currentTimeMillis();
		String token = Jwts.builder().setSubject(auth.getName())
				.claim("authorities",
						auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(now)).setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes()).compact();
		response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub

	}

	private class UserCredentials {
		private String username, password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public UserCredentials(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}
	}
}
