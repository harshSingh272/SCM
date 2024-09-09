package com.self.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.self.scm.entities.Providers;
import com.self.scm.entities.user;
import com.self.scm.helper.AppConstants;
import com.self.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
                logger.info("OAuthenticationSuccessHandler");

                var oauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;

                String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

                logger.info(authorizedClientRegistrationId);

                var oauthUser = (DefaultOAuth2User)authentication.getPrincipal();
                oauthUser.getAttributes().forEach((key, value)->{
                    logger.info(key + " : " + value);
                });

                user user1 = new user();
                user1.setUserId(UUID.randomUUID().toString());
                user1.setRoleList(List.of(AppConstants.ROLE_USER));
                user1.setEmailVarified(true);
                user1.setEnabled(true);
                user1.setPassword("Password");

                if(authorizedClientRegistrationId.equalsIgnoreCase("google")){

                    //google
                    //google attributes

                    user1.setEmail(oauthUser.getAttribute("email").toString());
                    user1.setProfiePic(oauthUser.getAttribute("picture").toString());
                    user1.setName(oauthUser.getAttribute("name").toString());
                    user1.setProviderId(oauthUser.getName());
                    user1.setProviders(Providers.GOOGLE);
                    user1.setAbout("This account is created using google");

                }


                else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){


                    String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString() : oauthUser.getAttribute("login").toString()+"@gmail.com";
                    String picture = oauthUser.getAttribute("avatar_url").toString();
                    String name=oauthUser.getAttribute("login").toString();
                    String phone=oauthUser.getAttribute("phone").toString();
                    String providerUserId = oauthUser.getName();

                    user1.setEmail(email);
                    user1.setProfiePic(picture);
                    user1.setName(name);
                    user1.setProviderId(providerUserId);
                    user1.setProviders(Providers.GITHUB);
                    user1.setPhoneNumber(phone);
                    user1.setAbout("This account is created using github");
                }


                //data saveing to datebase
                /* 
                DefaultOAuth2User user =  (DefaultOAuth2User)authentication.getPrincipal();

                // user.getAttributes().forEach((key,value)->{
                //     logger.info("{}:{}", key, value);
                // });
                
                // logger.info(user.getAuthorities().toString());

                String email=user.getAttribute("email").toString();
                String name=user.getAttribute("name").toString();
                String picture=user.getAttribute("picture").toString();

                //create user and save to database
                user user1 = new user();
                user1.setEmail(email);
                user1.setName(name);
                user1.setProfiePic(picture);
                user1.setPassword("password");
                user1.setUserId(UUID.randomUUID().toString());
                user1.setProviders(Providers.GOOGLE);
                user1.setEnabled(true);
                user1.setEmailVarified(true);
                user1.setProviderId(user.getName());
                user1.setRoleList(List.of(AppConstants.ROLE_USER));
                user1.setAbout("This acount is created using google login");
                */

                user user2 = userRepo.findByEmail(user1.getEmail()).orElse(null);
                if(user2==null){
                    userRepo.save(user1);
                    logger.info("user saved" + user1.getEmail());
                }

                new DefaultRedirectStrategy().sendRedirect(request, response,"/user/dashboard");
    }

}
