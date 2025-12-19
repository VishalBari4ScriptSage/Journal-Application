package org.ejournal.app.module.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.ejournal.app.module.entity.User;
import org.ejournal.app.module.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public List<User> allUserDetails(){
		return new ArrayList<>(userRepository.findAll());
	}
	
	public User createUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setUserRoles(new ArrayList<>(Arrays.asList("USER")));
		user.setCreateDate(LocalDateTime.now());
		return userRepository.save(user);
	}
	
	public User createAdmin(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setUserRoles(new ArrayList<>(Arrays.asList("ADMIN","USER")));		 
		user.setCreateDate(LocalDateTime.now());
		return userRepository.save(user);
	}


	public User savedUser(User user) {
		return userRepository.save(user);
	}
	
	public User findUserByUserid(ObjectId id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}
	
	public void updateUser(User user) {
		user.setUpdateDate(LocalDateTime.now());
		userRepository.save(user);
	}
	
	public boolean deleteUserByUsername(String username) {
		return userRepository.deleteUserByUsername(username);
	}

	public void deleteUserByUserid(ObjectId id) {
		userRepository.deleteById(id);
	}

}
