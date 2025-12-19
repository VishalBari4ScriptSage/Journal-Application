package org.ejournal.app.module.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.ejournal.app.module.entity.JournalEntry;
import org.ejournal.app.module.entity.User;
import org.ejournal.app.module.service.JournalService;
import org.ejournal.app.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/journal")
@Slf4j
public class JournalController {

	@Autowired
	private JournalService journalService;
	
	@Autowired
	private UserService userService;
	
	private String errorMsg = "Error - Unauthorize attempt";

	@Transactional
	@GetMapping("get-user-entry/all")
	public ResponseEntity<?> getUserAllEntry(){
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String authUsername = authentication.getName();
			User user = userService.findUserByUsername(authUsername);
			
			if(user != null) {
				List<JournalEntry> entryList = user.getJournalEntries().stream().toList();
				if(!entryList.isEmpty()) {
					log.info("search : {} user journal entries in database : access by {}",user.getUsername(),authUsername);
					return new ResponseEntity<>(entryList, HttpStatus.FOUND);									
				}
				log.error("search : {} user journal entries not found in database : access by {}",user.getUsername(),authUsername);
				return new ResponseEntity<>("Error - No user journal entry found : Something Went Wrong !!!", HttpStatus.NOT_FOUND);
			}
			log.error("un-authorized access : journal entries : access by {}",authUsername);
			return new ResponseEntity<>(errorMsg, HttpStatus.UNAUTHORIZED);
	}

	@Transactional
	@GetMapping("/get-user-entry/id/{id}")
	public ResponseEntity<?> getUserEntryByEntryId(@PathVariable ObjectId id){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
		User user = userService.findUserByUsername(authUsername);
		
		if(user != null) {
			boolean isExist = user.getJournalEntries().stream().anyMatch(x ->x.getId().equals(id));
			if(isExist) {
				JournalEntry userEntry = journalService.findUserEntryByEntryId(id);
				log.info("search : {} journal entries in database : access by {}",user.getUsername(),id,authUsername);
				return new ResponseEntity<>(userEntry, HttpStatus.FOUND);
			}
			log.error("search : {} journal entries not found in database : access by {}",user.getUsername(),id,authUsername);
			return new ResponseEntity<>("Error - No journal entry found in user : Something Went Wrong !!!", HttpStatus.NOT_FOUND);
		}
		log.error("un-authorized access : {} journal entries : access by {}",id,authUsername);
		return new ResponseEntity<>(errorMsg, HttpStatus.UNAUTHORIZED);
	}

	
	@Transactional
	@GetMapping("/get-user-entry/username/{username}")
	public ResponseEntity<?> getUserEntryByEntryName(@PathVariable String username){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
		User user = userService.findUserByUsername(authUsername);
		
		if(user != null) {
			JournalEntry entry = user.getJournalEntries().stream().filter(x -> x.getName().equals(username)).findFirst().orElse(null);
			if(entry != null) {
				log.info("search : {} journal entries in database : access by {}",user.getUsername(),username,authUsername);
				JournalEntry userEntry = journalService.findUserEntryByEntryId(entry.getId());
				return new ResponseEntity<>(userEntry, HttpStatus.FOUND);									
			}
			log.error("search : {} journal entries not found in database : access by {}",user.getUsername(),username,authUsername);
		return new ResponseEntity<>("Error - No journal entry found in user : Something Went Wrong !!!", HttpStatus.NOT_FOUND);
		}
		log.error("un-authorized access : {} journal entries : access by {}",username,authUsername);
		return new ResponseEntity<>(errorMsg, HttpStatus.UNAUTHORIZED);
	}

	
	@Transactional
	@PostMapping("/create-user-entry")
	public ResponseEntity<?> createUserJournalEntry(@RequestBody JournalEntry entry) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
		User user = userService.findUserByUsername(authUsername);
		
		if(user != null) {
			JournalEntry savedEntry = journalService.createUserJournalEntry(entry);
			if(savedEntry != null) {
				user.getJournalEntries().add(savedEntry);
				userService.savedUser(user);
				log.info("created {} journal entry in database : access by {}",savedEntry.getId(),authUsername);
				return new ResponseEntity<>(savedEntry.getId() +"Journal Entry created successfully", HttpStatus.CREATED);
			}
			log.error("not created {} journal entry in database : access by {}",savedEntry.getId(),authUsername);
			return new ResponseEntity<>("Error - Journal entry not created : Something Went Wrong!!!", HttpStatus.NOT_FOUND);
		}
		log.error("un-authorized access : not created journal entry in database : access by {}",user.getUsername(),authUsername);
		return new ResponseEntity<>(errorMsg, HttpStatus.UNAUTHORIZED);
	}


	@Transactional
	@PutMapping("/update-user-entry/id/{id}")
	public ResponseEntity<?> updateUserJournalEntry(@RequestBody JournalEntry newEntry, @PathVariable ObjectId id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
		User user = userService.findUserByUsername(authUsername);
		
		if(user != null) {
			boolean isExist = user.getJournalEntries().stream().anyMatch(x ->x.getId().equals(id));
			if(isExist) {
				JournalEntry oldEntry = journalService.findUserEntryByEntryId(id);
				if(oldEntry != null) {
					oldEntry.setName(!newEntry.getName().isEmpty() ? newEntry.getName() : oldEntry.getName());
					oldEntry.setName(!newEntry.getStory().isEmpty() ? newEntry.getStory() : oldEntry.getStory());
					JournalEntry savedEntry = journalService.updateUserJournalEntry(oldEntry);
					user.getJournalEntries().add(savedEntry);
					userService.savedUser(user);
					log.info("{} is search in databse : search by {}",user.getUsername(),authUsername);
					return new ResponseEntity<>(oldEntry.getId() +" Journal entry update successfully", HttpStatus.OK);
				}
				log.error("{} is not found OR {} not in database : search by {}",user.getId(),authUsername);
				return new ResponseEntity<>("Error - Journal entry not update : Something Went Wrong!!!", HttpStatus.NOT_FOUND);				
			}
			log.error("{} is not found OR {} not in database : search by {}",user.getId(),authUsername);
			return new ResponseEntity<>("Error - Journal entry not found in user : Something Went Wrong!!!", HttpStatus.NOT_FOUND);
		}
		log.error("{} is not found OR {} not in database : search by {}",user.getId(),authUsername);
		return new ResponseEntity<>(errorMsg, HttpStatus.UNAUTHORIZED);
	}	


	@Transactional
	@DeleteMapping("/delete-user-entry/id/{id}")
	public ResponseEntity<?> deleteUserJournalEntry(@PathVariable ObjectId id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
		User user = userService.findUserByUsername(authUsername);
		
		if(user != null) {
			boolean isExist = user.getJournalEntries().stream().anyMatch(x ->x.getId().equals(id));
			if(isExist) {
				journalService.deleteUserJournalEntry(id);
				boolean isDelete = user.getJournalEntries().removeIf(x ->x.getId().equals(id));
				if(isDelete) {
					userService.savedUser(user);
					log.info("{} is search in databse : search by {}",user.getUsername(),authUsername);
					return new ResponseEntity<>(id+"Journal entry deleted successfully", HttpStatus.OK);				
				}
				log.error("{} is not found OR {} not in database : search by {}",user.getId(),authUsername);
				return new ResponseEntity<>("Error - Journal entry not delete : Something Went Wrong!!!", HttpStatus.NOT_FOUND);				
			}
			log.error("{} is not found OR {} not in database : search by {}",user.getId(),authUsername);
			return new ResponseEntity<>("Error - Journal entry not found in user : Something Went Wrong!!!", HttpStatus.NOT_FOUND);
		}
		log.error("{} is not found OR {} not in database : search by {}",user.getId(),authUsername);
		return new ResponseEntity<>(errorMsg, HttpStatus.UNAUTHORIZED);
	}	

	
}
