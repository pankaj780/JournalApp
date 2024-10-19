package com.comeonshukla.JournalApp.Services;

import com.comeonshukla.JournalApp.Entity.User;
import com.comeonshukla.JournalApp.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Component
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void saveNewUser(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
        }catch (Exception e){
            log.error("saveNewUser.",e.getMessage());
        }

    }
    public void saveAdminUser(User user){
        try {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
        }catch (Exception e){
            log.error("saveAdminUser",e.getMessage());
        }
    }

    public void saveUser(User user){
        try {
            userRepository.save(user);
        }catch (Exception e){
            log.error("saveUser",e.getMessage());
        }
    }

    public Optional<User> getById(ObjectId id){
       return userRepository.findById(id);

    }

    public boolean deleteEntry(ObjectId id){
        userRepository.deleteById(id);
        return true;
    }

    public User findByUserName(String username){
        return userRepository.findByUsername(username);

    }


}
