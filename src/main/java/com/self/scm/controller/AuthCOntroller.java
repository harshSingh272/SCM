package com.self.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.self.scm.entities.user;
import com.self.scm.helper.MessageType;
import com.self.scm.helper.message;
import com.self.scm.repositories.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthCOntroller {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session){


        user user2= userRepo.findByEmailToken(token).orElse(null);

        if(user2 != null){

            if(user2.getEmailToken().equals(token)){
                user2.setEmailVarified(true);
                user2.setEnabled(true);
                userRepo.save(user2);
                var errMessage = message.builder().content("account verified, you can now login !!").type(MessageType.green).build();
                session.setAttribute("message", errMessage);
                return "success_page";
            }

            return "error_page";
        }

        var errMessage = message.builder().content("Something went wrong, account verification failed").type(MessageType.red).build();
        session.setAttribute("message", errMessage);
        return "error_page";
    }
}
