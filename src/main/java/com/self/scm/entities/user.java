package com.self.scm.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="user")
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class user implements UserDetails{

    @Id
    private String UserId;
    @Column(name = "user_name", nullable=false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Column(length=1000)
    private String about;
    @Column(length=1000)
    private String profiePic;
    private String phoneNumber;

    @Getter(value = AccessLevel.NONE)
    private boolean enabled=false;
    private boolean emailVarified=false;
    private boolean phoneVerified=false;

    
    // GOOGLE, GITHUB
    @Enumerated(value = EnumType.STRING)
    private Providers providers=Providers.SELF;
    private String providerId;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts=new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    private String emailToken;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //list of roles(USER, ADMIN)
        //collection of SimpleGrantedAuthority[roles{ADMIN, USER}]
        Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }

    @Override
    public String getUsername() {
        //emailId --> username
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
       return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}