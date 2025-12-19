package org.ejournal.app.module.controller;

import java.util.ArrayList;
import java.util.List;

import org.ejournal.app.module.entity.JournalEntry;
import org.ejournal.app.module.entity.User;
import org.ejournal.app.module.service.JournalService;
import org.ejournal.app.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JournalService journalService;

	@GetMapping("/all-user")
	public ResponseEntity<String> getAllUserDetails(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
		
		try {
			List<User> userList = new ArrayList<>(userService.allUserDetails());
			if(!userList.isEmpty()) {
				log.info("search : found all user journal entries in database : access by {} admin",authUsername);
				return new ResponseEntity<>("User list found : "+userList , HttpStatus.FOUND);				
			}
			log.error("un-authorized search : not found all user journal entries in database : access by {} admin",authUsername);
			return new ResponseEntity<>("No user found - Empty user list !!!", HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			log.error("Exception occure : not found all user journal entries in database : access by {} admin",authUsername,e);
			return new ResponseEntity<>("Exception Occure : all user details !!!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/all-journal-entry")
	public ResponseEntity<String> getAllJournalEntries(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();

		try {
			List<JournalEntry> entryList = journalService.getAllJournalEntry();
			if(!entryList.isEmpty()) {
				log.info("search : found all user journal entries in database : access by {} admin",authUsername);
				return new ResponseEntity<>("All journal entries found : "+entryList , HttpStatus.FOUND);				
			}
			log.error("un-authorized search : not found all user journal entries in database : access by {} admin",authUsername);
			return new ResponseEntity<>("No entry found - Empty journal list !!!", HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			log.error("Exception occure : not found all user journal entries in database : access by {} admin",authUsername,e);
			return new ResponseEntity<>("Exception Occure : all journal entry !!!", HttpStatus.BAD_REQUEST);
		}
	}


	@PostMapping("/create-admin")
	public ResponseEntity<String> createUser(@RequestBody User user){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
		
		try {
			User savedUser = userService.createAdmin(user);
			if(savedUser != null) {
				log.info("created {} : {} user in database : access from public",savedUser.getId(),savedUser.getUsername());
				return new ResponseEntity<>("User Created with id : " + savedUser.getId(), HttpStatus.CREATED);				
			}	
			log.error("un-authorized create : not create {} user in database : access by {} admin",user.getUsername(),authUsername);
			return new ResponseEntity<>("Error - User not Created : Something went wrong !!! " , HttpStatus.NOT_ACCEPTABLE);
		}
		catch(Exception e) {
			log.error("Exception occure : not create {} user in database : access by {} admin",user.getUsername(),authUsername,e);
			return new ResponseEntity<>("Exception Occure : creating admin !!!", HttpStatus.BAD_REQUEST);			
		}
	}

	
	
}
