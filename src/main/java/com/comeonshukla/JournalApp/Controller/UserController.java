package com.comeonshukla.JournalApp.Controller;

import com.comeonshukla.JournalApp.Entity.User;
import com.comeonshukla.JournalApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
     List<User> data=userService.getUsers();
     if(!data.isEmpty()){
         return new ResponseEntity<>(data, HttpStatus.OK);
     }
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
     }

     @GetMapping("/byUsername")
     public ResponseEntity<User> getByUsername(){
         Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
         String username=authentication.getName();
         User users=userService.findByUserName(username);
         return new ResponseEntity<>(users,HttpStatus.OK);
     }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        User userInDb=userService.findByUserName(authentication.getName());
        if(userInDb!=null){
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userService.saveUser(userInDb);
        }
        return new ResponseEntity<>("Updated Successfully",HttpStatus.OK);
    }


}
