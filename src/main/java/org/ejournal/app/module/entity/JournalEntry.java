package org.ejournal.app.module.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="journal_entry")
@Data
public class JournalEntry {

	@Id
	private ObjectId id;
	
	private String name;
	private String story;
	private LocalDateTime creationData;
	private LocalDateTime updationData;
	
}
