package com.comeonshukla.JournalApp.Repository;

import com.comeonshukla.JournalApp.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


public class UserRepositoryImpl {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<User> getUserForSA(){
        Query query=new Query();
        query.addCriteria(Criteria.where("username").is("Admin"));
        List<User> users= mongoTemplate.find(query,User.class);
        return users;

     }
}
