package org.ejournal.app.module.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ejournal.app.module.entity.JournalEntry;
import org.ejournal.app.module.entity.User;
import org.ejournal.app.module.service.JournalService;
import org.ejournal.app.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JournalService journalService;
	
	@GetMapping("/all-user")
	public ResponseEntity<?> getAllUserDetails(){
		try {
			List<User> userList = new ArrayList<>(userService.allUserDetails());
			if(!userList.isEmpty()) {
				return new ResponseEntity<>(userList , HttpStatus.FOUND);				
			}
			return new ResponseEntity<>("No user found - Empty user list !!!", HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			return new ResponseEntity<>("Exception Occure !!!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/all-journal-entry")
	public ResponseEntity<?> getAllJournalEntries(){
		try {
			List<JournalEntry> entryList = journalService.getAllJournalEntry();
			if(!entryList.isEmpty()) {
				return new ResponseEntity<>(entryList , HttpStatus.FOUND);				
			}
			return new ResponseEntity<>("No entry found - Empty journal list !!!", HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			return new ResponseEntity<>("Exception Occure !!!", HttpStatus.BAD_REQUEST);
		}
	}


	@PostMapping("/create-admin")
	public ResponseEntity<String> createUser(@RequestBody User user){
		
		user.getUserRoles().addAll(Arrays.asList("ADMIN","USER"));		 
		User savedUser = userService.createUser(user);
		if(savedUser != null) {
			return new ResponseEntity<>("User Created with id : " + savedUser.getId(), HttpStatus.CREATED);				
		}
		return new ResponseEntity<>("Error - User not Created : Something went wrong !!! " , HttpStatus.NOT_ACCEPTABLE);
	}
	

	
	
}
