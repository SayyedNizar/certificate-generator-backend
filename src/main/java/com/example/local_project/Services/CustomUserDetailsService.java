package com.example.local_project.Services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.local_project.Entity.Users;
import com.example.local_project.Repository.UsersRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Here, 'username' is actually the user's email
        Users user = usersRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        
        // Create a list of authorities (roles)
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    
}
