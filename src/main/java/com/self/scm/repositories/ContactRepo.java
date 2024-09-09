package com.self.scm.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.self.scm.entities.Contact;
import com.self.scm.entities.user;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String>{

    //find the contacts by user;
    //custom finder method
    Page<Contact> findByUser(user user, Pageable pageable);


    //custom query method
//    @Query("SELECT c From Contact c WHERE c.user_user_id = :userId")
//    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByNameContainingAndUser(String nameKeyword, user user, Pageable pageable);

    Page<Contact> findByEmailContainingAndUser(String emailKeyword, user user, Pageable pageable);

    Page<Contact> findByPhoneNumberContainingAndUser(String phoneKeyword, user user, Pageable pageable);

}
