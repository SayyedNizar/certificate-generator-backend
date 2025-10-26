package com.example.local_project.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.local_project.Entity.Users;
import com.example.local_project.Repository.UsersRepo;
import com.example.local_project.Services.JwtService;
import com.example.local_project.Services.UserService;
import com.example.local_project.dto.AuthRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService usersService;
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersRepo usersRepo;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users user) {
        usersService.registerUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

 @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            // Find the user by email to get their ID
            Users user = usersRepo.findByEmail(authRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found during token creation"));
            
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            
            // 3. Pass the username, roles, AND the userId
            return jwtService.generateToken(authRequest.getUsername(), roles, user.getId());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}