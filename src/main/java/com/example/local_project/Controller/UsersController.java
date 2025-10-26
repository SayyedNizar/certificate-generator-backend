package com.example.local_project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.local_project.Entity.Users;
import com.example.local_project.Services.UserService;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserService userService;

    /**
     * --- THIS IS THE FIX ---
     * We now allow both ADMINs and INSTRUCTORs to fetch the user list.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('INSTRUCTOR')")
    public Page<Users> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Users getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Users updateUser(@PathVariable Long id, @RequestBody Users userDetails) {
        return userService.updateUser(id, userDetails);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
}

