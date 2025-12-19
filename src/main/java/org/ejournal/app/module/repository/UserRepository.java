package org.ejournal.app.module.repository;

import org.bson.types.ObjectId;
import org.ejournal.app.module.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>{

	User findUserByUsername(String username);
	
	boolean deleteUserByUsername(String username);
	
}
