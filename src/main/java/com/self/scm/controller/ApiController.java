package com.self.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.self.scm.entities.Contact;
import com.self.scm.entities.user;
import com.self.scm.services.ContactService;
import com.self.scm.services.UserService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;
     @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/contacts/{contactId}")
    public Contact getContact(@PathVariable String contactId){
        return contactService.getById(contactId);
    }

    @GetMapping("/user/{userId}")
    public user getUser(@PathVariable String userId){
        return userService.getUserById(userId).orElse(null);
    }

    @GetMapping("/password/{pass}")
    public String password(@PathVariable String pass){
        return passwordEncoder.encode(pass);
    }
}
