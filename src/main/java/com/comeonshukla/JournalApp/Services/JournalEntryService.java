package com.comeonshukla.JournalApp.Services;

import com.comeonshukla.JournalApp.Entity.JournalEntry;
import com.comeonshukla.JournalApp.Entity.User;
import com.comeonshukla.JournalApp.Repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    public List<JournalEntry> getEntries(){
        return journalEntryRepository.findAll();
    }

    @Transactional
    public boolean saveEntry(JournalEntry journalEntry,String username){
        try {
            User user = userService.findByUserName(username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
            return true;
        }catch (Exception e){
            throw new RuntimeException("An error occurred while saving the data"+e);
        }
    }
    public boolean updateEntry(JournalEntry journalEntry){
        JournalEntry saved= journalEntryRepository.save(journalEntry);
        return true;
    }

    public Optional<JournalEntry> getById(ObjectId id){
       return journalEntryRepository.findById(id);
    }

    public boolean deleteEntry(ObjectId id,String username){
        try {
            User user = userService.findByUserName(username);
            boolean deleted = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (deleted) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
                return true;
            }
            return false;
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
            //return false;
        }
    }



}
