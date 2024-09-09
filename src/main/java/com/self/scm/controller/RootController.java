package com.self.scm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.self.scm.entities.user;
import com.self.scm.helper.Helper;
import com.self.scm.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService UserService;

     @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication){
        if(authentication==null)
            return;
        String username = Helper.getEmailOfLoggedInUser(authentication);
        System.out.println("adding logged in user info to the model");
        logger.info("user logged in {}", username);
        //fetch data from database: get user form db:
        user user = UserService.getUserByEmail(username);
        System.out.println(user);
        
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        model.addAttribute("loggedInUser", user);    
    }
}
