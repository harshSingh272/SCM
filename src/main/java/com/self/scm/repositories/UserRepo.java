package com.self.scm.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.self.scm.entities.user;

@Repository
public interface UserRepo extends JpaRepository<user, String> {

    Optional<user> findByEmail(String email);
    
    Optional<user> findByEmailAndPassword(String email, String password);

    Optional<user> findByEmailToken(String token);
}
