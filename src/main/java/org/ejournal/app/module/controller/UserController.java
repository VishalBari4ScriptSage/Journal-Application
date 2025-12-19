package org.ejournal.app.module.controller;

import org.bson.types.ObjectId;
import org.ejournal.app.module.entity.User;
import org.ejournal.app.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/get-user/userid/{id}")
	public ResponseEntity<?> getUserByUserid(@PathVariable ObjectId id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
		
		User user = userService.findUserByUserid(id);
		if(user != null) {
			log.info("{} is search in databse : search by {}",user.getUsername(),authUsername);
			return new ResponseEntity<>("User found in database : "+user, HttpStatus.FOUND);
		}
		log.error("{} is not found OR {} not in database : search by {}",id,authUsername);
		return new ResponseEntity<>("Error - User Not Found", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/get-user/username/{username}")
	public ResponseEntity<String> getUserByUserid(@PathVariable String username){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();

		User user = userService.findUserByUsername(username);
		if(user != null) {
			log.info("{} is search in database : search by {}",user.getUsername(),authUsername);
			return new ResponseEntity<>("User found in database : "+user, HttpStatus.FOUND);
		}
		log.error("{} is not found OR {} not in database : search by {}",username,authUsername);
		return new ResponseEntity<>("Error - User Not Found", HttpStatus.NOT_FOUND);
	}
	
		
	@PutMapping("/update-user/username/{username}")
	public ResponseEntity<String> updateUserByUsername(@PathVariable String username, @RequestBody User newUser){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();

		User oldUser = userService.findUserByUsername(username);
		if(oldUser != null) {
			oldUser.setUsername((!newUser.getUsername().isEmpty())? newUser.getUsername(): oldUser.getUsername());
			oldUser.setPassword((!newUser.getPassword().isEmpty())? newUser.getPassword(): oldUser.getPassword());
			userService.updateUser(oldUser);
			log.info("{} is update in databse : update by {}",oldUser.getUsername(),username);
			return new ResponseEntity<>(oldUser.getUsername() +"details update successfully", HttpStatus.OK);							
		}
		log.error("{} is not update in database : update by {}",username,authUsername);
		return new ResponseEntity<>("Error - user not found to update !!!", HttpStatus.NOT_FOUND);			
	}

	@PutMapping("/update-user/id/{id}")
	public ResponseEntity<String> updateUserByUsername(@PathVariable ObjectId id, @RequestBody User newUser){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();

		User oldUser = userService.findUserByUserid(id);
		if(oldUser != null) {
			oldUser.setUsername((!newUser.getUsername().isEmpty())? newUser.getUsername(): oldUser.getUsername());
			oldUser.setPassword((!newUser.getPassword().isEmpty())? newUser.getPassword(): oldUser.getPassword());
			userService.updateUser(oldUser);
			log.info("{} is update in databse : update by {}",oldUser.getUsername(),authUsername);
			return new ResponseEntity<>(oldUser.getUsername() +"details update successfully", HttpStatus.OK);							
		}
		log.error("{} is not update in database : update by {}",id,authUsername);
		return new ResponseEntity<>("Error - user not found to update !!!", HttpStatus.NOT_FOUND);			
	}

	@DeleteMapping("/delete-user/id/{id}")
	public ResponseEntity<String> deleteUserByUsername(@PathVariable ObjectId id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();

		User user = userService.findUserByUserid(id);
		if(user != null) {
			userService.deleteUserByUserid(id);
			log.info("{} is delete in database : update by {}",user.getUsername(),authUsername);
			return new ResponseEntity<>(user.getUsername() +" is Deleted... ",HttpStatus.OK);				
		}
		log.error("{} is not delete in database : update by {}",id,authUsername);
		return new ResponseEntity<>("Error - User not found to delete !!!",HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/delete-user/username/{username}")
	public ResponseEntity<String> deleteUserByUsername(@PathVariable String username){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();

		boolean isDelete = userService.deleteUserByUsername(username);
		if(isDelete) {
			log.info("{} is delete in database : update by {}",username,authUsername);
			return new ResponseEntity<>(username +" is Deleted... ",HttpStatus.OK);				
		}
		log.error("{} is not delete in database : update by {}",username,authUsername);
		return new ResponseEntity<>("Error - User not found to delete !!!",HttpStatus.NOT_FOUND);
	}
	
		
}
