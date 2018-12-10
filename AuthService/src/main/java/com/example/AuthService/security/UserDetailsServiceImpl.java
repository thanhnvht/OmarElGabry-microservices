package com.example.AuthService.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final List<AppUser> users = new ArrayList<AppUser>();
		users.add(new AppUser(1,"user_nik",bCryptPasswordEncoder.encode("password"),"USER"));
		users.add(new AppUser(2,"admin_nik",bCryptPasswordEncoder.encode("password"),"ADMIN"));
		for(AppUser appUser: users) {
			if(appUser.getUsername().equals(username)) {
				List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_"+	appUser.getRole());
				return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
			}
		}
		
		throw new UsernameNotFoundException("Username: "+username+" not found");		
	}
	
	private static class AppUser{
		public AppUser(Integer id, String username, String password, String role) {
			super();
			this.id = id;
			this.username = username;
			this.password = password;
			this.role = role;
		}

		private Integer id;
		private String username, password;
		private String role;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
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
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
	}
}
