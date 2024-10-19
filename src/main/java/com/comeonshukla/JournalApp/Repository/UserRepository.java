package com.comeonshukla.JournalApp.Repository;

import com.comeonshukla.JournalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User , ObjectId> {
   User findByUsername(String username);
}
