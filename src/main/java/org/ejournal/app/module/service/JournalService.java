package org.ejournal.app.module.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.ejournal.app.module.entity.JournalEntry;
import org.ejournal.app.module.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalService {

	@Autowired
	private JournalRepository journalRepository;
		
	public List<JournalEntry> getAllJournalEntry(){
		return new ArrayList<>(journalRepository.findAll());
	}
	
	public JournalEntry findUserEntryByEntryId(ObjectId id) {
		return journalRepository.findById(id).orElse(null);
	}
		
	public JournalEntry createUserJournalEntry(JournalEntry entry) {
		entry.setCreationData(LocalDateTime.now());
		return journalRepository.save(entry);
	}
	
	public JournalEntry updateUserJournalEntry(JournalEntry entry) {
		entry.setUpdationData(LocalDateTime.now());
		return journalRepository.save(entry);
	}

	public void deleteUserJournalEntry(ObjectId id) {
		journalRepository.deleteById(id);
	}	
	
}
