package org.ejournal.app.module.repository;

import org.bson.types.ObjectId;
import org.ejournal.app.module.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalRepository extends MongoRepository<JournalEntry, ObjectId>{

	
}
