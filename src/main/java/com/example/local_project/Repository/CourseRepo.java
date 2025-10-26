package com.example.local_project.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.local_project.Entity.Courses;

public interface CourseRepo extends JpaRepository<Courses, Long> {
    
    Page<Courses> findByInstructorEmail(String email, Pageable pageable);
}
