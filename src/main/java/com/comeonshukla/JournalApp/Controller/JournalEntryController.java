package com.comeonshukla.JournalApp.Controller;

import com.comeonshukla.JournalApp.Entity.JournalEntry;
import com.comeonshukla.JournalApp.Entity.User;
import com.comeonshukla.JournalApp.Services.JournalEntryService;
import com.comeonshukla.JournalApp.Services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsersJournal(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user= userService.findByUserName(username);
        List<JournalEntry> data=user.getJournalEntries();
     if(!data.isEmpty()){
         return new ResponseEntity<>(data, HttpStatus.OK);
     }
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> createEntry(@RequestBody JournalEntry myEntry){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String username=authentication.getName();
            journalEntryService.saveEntry(myEntry,username);
            return new ResponseEntity<>("Journal Entry created Successfully",HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_GATEWAY);
        }
    }


    @GetMapping("id/{id}")
    public ResponseEntity<?> getById(@PathVariable ObjectId id){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUserName(username);
        List<JournalEntry> collect=user.getJournalEntries().stream().filter(x->x.getId().equals(id)).toList();
        if (!collect.isEmpty()){
            JournalEntry data= journalEntryService.getById(id).orElse(null);
            return new ResponseEntity<>(data,HttpStatus.OK);
        }
        return new ResponseEntity<>("Records not found.",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<String> deleteEntry(@PathVariable ObjectId id){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        boolean deleted=journalEntryService.deleteEntry(id,username);
         if (deleted){
          return new ResponseEntity<>("Deleted Successfully",HttpStatus.NO_CONTENT);
          }
      return new ResponseEntity<>("",HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<String> updateEntry(@RequestBody JournalEntry journalReq,
                                        @PathVariable ObjectId id){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUserName(username);
        List<JournalEntry> collect=user.getJournalEntries().stream().filter(x->x.getId().equals(id)).toList();
        if(!collect.isEmpty()){
            JournalEntry old=journalEntryService.getById(id).orElse(null);
                old.setTitle(journalReq.getTitle() != null ? journalReq.getTitle() : old.getTitle());
                old.setContent(journalReq.getContent() != null ? journalReq.getContent() : old.getContent());
                boolean updated = journalEntryService.updateEntry(old);
                if (updated) {
                    return new ResponseEntity<>("Records Updated successfully.", HttpStatus.OK);
                }
        }
        return new ResponseEntity<>("Not found.",HttpStatus.NOT_FOUND);
    }


}
