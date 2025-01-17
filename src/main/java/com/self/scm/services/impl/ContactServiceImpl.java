package com.self.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.self.scm.entities.Contact;
import com.self.scm.entities.user;
import com.self.scm.helper.ResouceNotFoundException;
import com.self.scm.repositories.ContactRepo;
import com.self.scm.repositories.UserRepo;
import com.self.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        if (contact.getContactImage().isEmpty()) {
            // Handle the case where no file is uploaded
            // For example, you can set a default image:
            contact.setContactImage("/static/Images/user.png");
        }
        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
       
        var contactOld = contactRepo.findById(contact.getId()).orElseThrow(() -> new ResouceNotFoundException("COntaction not found"));
        contactOld.setName(contact.getName());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setInstaLink(contact.getInstaLink());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setContactImage(contact.getContactImage());
        contactOld.setCloudinaryPublicImageId(contact.getCloudinaryImagePublicId());

        return contactRepo.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
        
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new ResouceNotFoundException("contact not found with id: "+ id));
    }

    @Override
    public void delete(String id) {
       var contactId=contactRepo.findById(id).orElseThrow(()-> new ResouceNotFoundException("contact not found with id: "+ id));
       
       contactRepo.delete(contactId);
    }

    // @Override
    // public List<Contact> getByUserId(String userId) {
    //     user user1 = userRepo.findById(userId).orElse(null);
    //     return contactRepo.findByUser(user1);
    // }

    @Override
    public Page<Contact> getByUser(user user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page, size, sort);


        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searcByName(String nameKeyword, int size, int page, String sortBy, String order, user user) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByNameContainingAndUser(nameKeyword, user, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, user user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByEmailContainingAndUser(emailKeyword, user, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order, user user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByPhoneNumberContainingAndUser(phoneNumberKeyword, user, pageable);
    }

   

   
}
