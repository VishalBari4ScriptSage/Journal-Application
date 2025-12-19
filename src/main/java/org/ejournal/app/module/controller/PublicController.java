package org.ejournal.app.module.controller;

import org.ejournal.app.module.entity.User;
import org.ejournal.app.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {
	
	@Autowired
	private UserService userService;

	@PostMapping("/create-user")
	public ResponseEntity<String> createUser(@RequestBody User user){
		try {
			User savedUser = userService.createUser(user);
			if(savedUser != null) {
				log.info("created {} : {} user in database : access from public",savedUser.getId(),savedUser.getUsername());
				return new ResponseEntity<>("User Created with id : " + savedUser.getId(), HttpStatus.CREATED);				
			}
			log.error("not created : {} user is in database : access from public",user.getUsername());
			return new ResponseEntity<>("Error - User not Created : Something went wrong !!! " , HttpStatus.NOT_ACCEPTABLE);
		}
		catch(Exception e) {
			log.error("Exception occure : {} is not created : access from public",user.getUsername(),e);
			return new ResponseEntity<>("Exception Occure",HttpStatus.BAD_REQUEST);			
		}
	}
	

	
}
