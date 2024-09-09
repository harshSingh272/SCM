package com.self.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.self.scm.helper.AppConstants;
import com.self.scm.helper.Helper;
import com.self.scm.helper.ResouceNotFoundException;
import com.self.scm.entities.user;
import com.self.scm.entities.user;
import com.self.scm.repositories.UserRepo;
import com.self.scm.services.EmailService;
import com.self.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService{


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger=LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmailService emailService;

    @Override
    public user saveUser(user userNew) {
        //user id have to be generated
        String userId = UUID.randomUUID().toString();
        userNew.setUserId(userId);

        userNew.setPassword(passwordEncoder.encode(userNew.getPassword()));
        
        userNew.setRoleList(List.of(AppConstants.ROLE_USER));
        logger.info(userNew.getProviders().toString());

        String emailToken=UUID.randomUUID().toString();
        userNew.setEmailToken(emailToken);
        user saveUser= userRepo.save(userNew);
        String emailLink=Helper.getLinkForEmailVerification(emailToken);
        emailService.sendEmail(saveUser.getEmail(), "Verification email : SCM", emailLink);
        return saveUser;
    }

    @Override
    public Optional<user> getUserById(String id) {
        
        return userRepo.findById(id);
    }

    @Override
    public Optional<user> updateUser(user user) {
        
        user user2=userRepo.findById(user.getUserId()).orElseThrow(()-> new ResouceNotFoundException("User not found"));
        //update the user
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(passwordEncoder.encode(user.getPassword()));
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfiePic(user.getProfiePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVarified(user.isEmailVarified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProviders(user.getProviders());
        user2.setProviderId(user.getProviderId());

        //save the use in database
        user save = userRepo.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        user user2=userRepo.findById(id).orElseThrow(()-> new ResouceNotFoundException("User not found"));

        userRepo.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        user user2=userRepo.findById(userId).orElse(null);

        return user2!=null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        user user2 = userRepo.findByEmail(email).orElse(null);

        return user2!=null ? true : false;
    }

    @Override
    public List<user> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public user getUserByEmail(String email) {
        
        return userRepo.findByEmail(email).orElse(null);
    }

}
