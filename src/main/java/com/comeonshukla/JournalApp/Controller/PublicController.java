package com.comeonshukla.JournalApp.Controller;

import com.comeonshukla.JournalApp.Entity.User;
import com.comeonshukla.JournalApp.Services.MailService;
import com.comeonshukla.JournalApp.Services.UserDetailServiceImpl;
import com.comeonshukla.JournalApp.Services.UserService;
import com.comeonshukla.JournalApp.Utility.JwtUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authecationManager;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired(required=true)
    MailService mailService;

    @GetMapping("/")
    public String healthCheck(){
        return "Ok";
    }

    @PostMapping("/admin/createAdmin")
    public ResponseEntity<String> createAdminUser(@RequestBody User user){
        userService.saveAdminUser(user);
        return new ResponseEntity<>("Admin User created successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> createUser(@RequestBody User user){
        userService.saveNewUser(user);
        return new ResponseEntity<>("User created successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user){
        try {
            authecationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
           UserDetails userDetails= userDetailService.loadUserByUsername(user.getUsername());
           String jwt= jwtUtility.generateToken(user.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Something went wrong."+e);
            return new ResponseEntity<>("Something went wrong.", HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("sendMail")
    public void SendEmail(){
        mailService.sendMail("pks2221@yopmail.com","Greetings","Hello Pankaj");
    }

}
