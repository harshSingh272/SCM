package com.self.scm.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.self.scm.services.ContactService;
import com.self.scm.services.ImageService;
import com.self.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.self.scm.entities.Contact;
import com.self.scm.entities.user;
import com.self.scm.forms.ContactForm;
import com.self.scm.forms.ContactSearchForm;
import com.self.scm.helper.AppConstants;
import com.self.scm.helper.Helper;
import com.self.scm.helper.MessageType;
import com.self.scm.helper.message;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private  ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    //add contact page handler
    @RequestMapping("/add")
    public String addContactView(Model model){

        ContactForm contactForm = new ContactForm();

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("currentPage", "http://localhost:8081/user/contacts/add");

        return "/user/add_contact";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute("contactForm") ContactForm contactForm, @RequestParam("contactImage") MultipartFile file, BindingResult result, Authentication authentication, HttpSession session){

        System.out.println("processing registeration");
        //fetch form data
        System.out.println(contactForm);
        //validate form data
  
        if(result.hasErrors()){
            session.setAttribute("message", message.builder().content("Enter valid details").type(MessageType.red).build());
            return "/user/add_contact";
        }
        
        String username = Helper.getEmailOfLoggedInUser(authentication);
        user user = userService.getUserByEmail(username);
        //form --> contact

        //process the contact picture

        logger.info("file information: {}", contactForm.getContactImage().getOriginalFilename());

        Contact contact = new Contact();

        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setInstaLink(contactForm.getInstaLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());

        if (file != null && !file.isEmpty()) {
            logger.info("File is not empty");
            String fileName = UUID.randomUUID().toString();
            String imageURL = imageService.uploadImage(file, fileName);
            contact.setCloudinaryImagePublicId(fileName);
            contact.setContactImage(imageURL);
            contactForm.setPicture(imageURL);
        } else {
            logger.info("File is empty, using default image");
            String defaultImageURL = "https://i.pinimg.com/474x/65/25/a0/6525a08f1df98a2e3a545fe2ace4be47.jpg";
            contact.setContactImage(defaultImageURL);
            contact.setCloudinaryPublicImageId("default_profile_pic");
            contactForm.setPicture(defaultImageURL);
        }

        contactService.save(contact);
        System.out.println(contactForm);

        //set the contact page url


        //set the msg to be displayed on the view
        message messageAlert =  message.builder().content("Contact Added").type(MessageType.blue).build();

        session.setAttribute("message", messageAlert);
        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    public String viewContacts(
        @RequestParam(value="page", defaultValue = "0") int page, 
        @RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE) int size, 
        @RequestParam(value="sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value="direction", defaultValue = "asc") String direction, 
        Model model, Authentication authentication){

        //laod all the user contacts

        String username = Helper.getEmailOfLoggedInUser(authentication);
        user user = userService.getUserByEmail(username);
        Page<Contact> pageContacts = contactService.getByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("currentPage", "http://localhost:8081/user/contacts");
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute("contactSearchForm") ContactSearchForm contactSearchForm,
        @RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE) int size,
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value="order", defaultValue = "asc") String direction,
        Model model,
        Authentication authentication
    ){

        logger.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getValue());

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContacts=null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContacts = contactService.searcByName(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContacts=contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            pageContacts=contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction, user);
        }

        logger.info("pageContacts {}", pageContacts);
        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("contactSearchForm", contactSearchForm);

        return "user/search";
    }

    @RequestMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") String id) {

        contactService.delete(id);
        logger.info("contactId:{} deleted", id);

        return "redirect:/user/contacts";
    }
    
    @GetMapping("/view/{contactId}")
    public String updateContactFormView(
        @PathVariable("contactId") String contactId,
        Model model,
        Authentication authentication
    ){

        var contact=contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();
       
        contactForm.setName(contact.getName());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setInstaLink(contact.getInstaLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getContactImage());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        return "user/update_contact_view";
    }


    @RequestMapping(value = "/update/{contactId}", method=RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId,@Valid @ModelAttribute("contactForm") ContactForm contactForm, BindingResult bindingResult, Model model, HttpSession session, Authentication authentication) {


        if(bindingResult.hasErrors()){
            return "user/update_contact_view";
        }

        var contact = contactService.getById(contactId);
        contact.setId(contactId);
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setInstaLink(contactForm.getInstaLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());

        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty())
        {
            logger.info("file is not empty");
            String fileName = UUID.randomUUID().toString();
            String imageURL =  imageService.uploadImage(contactForm.getContactImage(), fileName);
            contact.setCloudinaryImagePublicId(fileName);
            contact.setContactImage(imageURL);
            contactForm.setPicture(imageURL);
        }else{
            logger.info("file is emoty");
        }
        
        var updtatedCon = contactService.update(contact);
        logger.info("updated contact: " + updtatedCon);
        message messageAlert =  message.builder().content("Contact updated").type(MessageType.blue).build();

        session.setAttribute("message", messageAlert);
        return "redirect:/user/contacts/view/"+contactId;
    }
}

