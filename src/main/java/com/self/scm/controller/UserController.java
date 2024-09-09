package com.self.scm.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.self.scm.entities.user;
import com.self.scm.forms.UserUpdateForm;
import com.self.scm.helper.MessageType;
import com.self.scm.helper.ResouceNotFoundException;
import com.self.scm.helper.message;
import com.self.scm.services.ImageService;
import com.self.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

     @Autowired
    private ImageService imageService;

    //user dashboard page

    @RequestMapping(value = "/dashboard")
    public String userDashboard(Model model) {
        model.addAttribute("currentPage", "http://localhost:8081/user/dashboard");
        System.out.println("user dashboard");
        return "user/dashboard";
    }

    @RequestMapping(value = "/profile")
    public String userProfile(Model model, Authentication authentication) {
        model.addAttribute("currentPage", "http://localhost:8081/user/profile");
        return "user/profile";
    }

    @GetMapping("/view/{userId}")
    public String updateUserFormView(
        @PathVariable("userId") String userId,
        Model model,
        Authentication authentication
    ){

        user user=userService.getUserById(userId).orElseThrow(() -> new ResouceNotFoundException("User not found"));
        UserUpdateForm userForm = new UserUpdateForm();
       
        userForm.setName(user.getName());
        userForm.setEmail(user.getEmail());
        userForm.setPhoneNumber(user.getPhoneNumber());
        userForm.setPicture(user.getProfiePic());
        userForm.setAbout(user.getAbout());
        model.addAttribute("userForm", userForm);
        model.addAttribute("userId", userId);

        return "update_user";
    }
    
     @RequestMapping(value = "/update/{userId}", method=RequestMethod.POST)
    public String updateuser(@PathVariable("userId") String userId,@Valid @ModelAttribute("userForm") UserUpdateForm userForm, BindingResult bindingResult, Model model, HttpSession session, Authentication authentication) {


        if(bindingResult.hasErrors()){
            return "update_user";
        }

        user user = userService.getUserById(userId).orElseThrow(() -> new ResouceNotFoundException("User not found"));
        user.setUserId(userId);
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setAbout(userForm.getAbout());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());

        if(userForm.getUserImage() != null && !userForm.getUserImage().isEmpty())
        {
            logger.info("file is not empty");
            String fileName = UUID.randomUUID().toString();
            String imageURL =  imageService.uploadImage(userForm.getUserImage(), fileName);
            user.setProfiePic(imageURL);
            userForm.setPicture(imageURL);
        }else{
            logger.info("file is emoty");
        }
        
        var updtatedCon = userService.updateUser(user);
        logger.info("updated user: " + updtatedCon);
        message messageAlert =  message.builder().content("user updated").type(MessageType.blue).build();

        session.setAttribute("message", messageAlert);
        return "redirect:/user/profile";
    }

    @GetMapping("/delete/{userId}")
    public String deleteUser(
        @PathVariable("userId") String userId,
        Model model,
        Authentication authentication
    ){
       userService.deleteUser(userId);
        return "redirect:/do-logout";
    }
}
    

