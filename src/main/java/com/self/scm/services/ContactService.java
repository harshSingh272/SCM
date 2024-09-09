package com.self.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import com.self.scm.entities.Contact;
import com.self.scm.entities.user;

public interface ContactService{

    //save contact
    Contact save(Contact contact);

    //update contact
    Contact update(Contact contact);

    //get contacts
    List<Contact> getAll();

    //get Contact by id
    Contact getById(String id);

    //delete contact
    void delete(String id);

    //search contact
    Page<Contact> searcByName(String nameKeyword,int size, int page, String sortBy, String order, user user);

    Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, user user);

    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order, user user);

    //get contacts by userid
    // List<Contact> getByUserId(String userId);
    Page<Contact> getByUser(user user,int page, int size, String sortBy, String direction);



}
