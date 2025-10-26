package com.example.local_project.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.local_project.Entity.Users;
import com.example.local_project.Repository.CertificateBatchRepo;
import com.example.local_project.Repository.CertificateRepo;
import com.example.local_project.Repository.UsersRepo;

@Service
public class UserService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CertificateRepo certificateRepo;
    @Autowired
    private CertificateBatchRepo certificateBatchRepo;

    /**
     * Registers a new user, checking for duplicate emails and encrypting the password.
     */
    public Users registerUser(Users user) {
        if (usersRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usersRepo.save(user);
    }

     public Users getUserById(Long id) {
        return usersRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * Fetches a paginated list of all users for the admin dashboard.
     */
    public Page<Users> getAllUsers(Pageable pageable) {
        return usersRepo.findAll(pageable);
    }

    /**
     * Safely deletes a user and all their associated records.
     * @Transactional ensures all delete operations succeed or fail together as a single unit.
     */
    @Transactional
    public String deleteUser(Long id) {
        if (!usersRepo.existsById(id)) {
            return "User not found with id: " + id;
        }
        
        try {
            // First, delete dependent records to avoid database errors.
            certificateRepo.deleteByUserId(id);
            certificateBatchRepo.deleteByRequestedById(id);
            
            // Now, it's safe to delete the user.
            usersRepo.deleteById(id);
            return "User and all associated data deleted successfully.";
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user with id " + id + ": " + e.getMessage(), e);
        }
    }

     public Users updateUser(Long id, Users userDetails) {
        Users existingUser = getUserById(id); // Reuse the method above to find the user

        existingUser.setName(userDetails.getName());
        existingUser.setRole(userDetails.getRole());
        
        return usersRepo.save(existingUser);
    }
}

