package org.ejournal.app.module.service;

import org.ejournal.app.module.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userService.findUserByUsername(username);

			return org.springframework.security.core.userdetails.User.builder()
					.username(user.getUsername())
					.password(passwordEncoder.encode(user.getPassword()))
					.roles(user.getUserRoles().get(0))
					.build();
				
		}
		catch(Exception e) {
			throw new UsernameNotFoundException("Exception Ocuure - Username not found !!!");
		}
	
	}	
	
	
}
