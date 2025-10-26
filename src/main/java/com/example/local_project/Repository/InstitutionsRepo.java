package com.example.local_project.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.local_project.Entity.Institutions;

@Repository
public  interface InstitutionsRepo extends JpaRepository<Institutions, Long> {
    
}