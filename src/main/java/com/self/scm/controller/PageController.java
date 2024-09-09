package com.self.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.self.scm.entities.user;
import com.self.scm.forms.UserForms;
import com.self.scm.helper.MessageType;
import com.self.scm.helper.message;
import com.self.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    

    @RequestMapping("/home")
    public String home(Model model){
        model.addAttribute("currentPage", "http://localhost:8081/home");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("isLogged", false);
        System.out.println("About page loading");
        model.addAttribute("currentPage", "http://localhost:8081/about");
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage(Model model){
        System.out.println("Services page loading");
        model.addAttribute("currentPage", "http://localhost:8081/services");
        return "services";
    }

    @RequestMapping("/contact")
    public String contactPage(Model model){
        System.out.println("Contact page loading");
        model.addAttribute("currentPage", "http://localhost:8081/contact");
        return "contact";
    }

    @RequestMapping("/login")
    public String login(){
        System.out.println("Login page loading");
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        System.out.println("Signup page loading");

        UserForms userForm = new UserForms();
        //default data
        model.addAttribute("userForm", userForm);

        return "signup";
    }
    

    // Processing register

    @RequestMapping(value="/do-register", method=RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute("userForm") UserForms userForm, BindingResult rBindingResult, HttpSession session){
        System.out.println("processing registeration");
        //fetch form data
        System.out.println(userForm);
        //validate form data
  
        if(rBindingResult.hasErrors()){
            return "signup";
        }

        //save to database

        //userservice
        //userForm --> user
        message messageAlert;
        if(userService.isUserExistByEmail(userForm.getEmail()))
        {
            messageAlert =  message.builder().content("User Exists").type(MessageType.blue).build();
        }
        else{
            user user2 = new user();
            user2.setName(userForm.getName());
            user2.setEmail(userForm.getEmail());
            user2.setPassword(userForm.getPassword());
            user2.setAbout(userForm.getAbout());
            user2.setEnabled(false);
            user2.setPhoneNumber(userForm.getPhoneNumber());
            user2.setProfiePic("/static/Images/user.png");


            user savedUser = userService.saveUser(user2);
            //redirect to login page
            System.out.println(savedUser);

            messageAlert =  message.builder().content("Registration Successful").type(MessageType.blue).build();
        }

        session.setAttribute("message", messageAlert);

        return "redirect:/signup";
    }
}

