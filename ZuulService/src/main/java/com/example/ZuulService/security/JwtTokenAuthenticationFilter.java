package com.example.ZuulService.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.ZuulService.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter{
	
	private final JwtConfig jwtConfig;

	public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String header = request.getHeader(jwtConfig.getHeader());
		
		if(header == null || !header.startsWith(jwtConfig.getPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = header.replace(jwtConfig.getPrefix(), "");
		
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(jwtConfig.getSecret().getBytes())
					.parseClaimsJws(token)
					.getBody();
			String usernamePrincipal = claims.getSubject();
			
			if(usernamePrincipal != null) {
				List<String> authorities = (List<String>) claims.get("authorities");
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						usernamePrincipal, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			
		}
		filterChain.doFilter(request, response);
		
	}
	

}
