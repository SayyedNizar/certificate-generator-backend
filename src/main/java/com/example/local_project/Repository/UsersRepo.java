package com.example.local_project.Repository;

import com.example.local_project.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; 

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
}