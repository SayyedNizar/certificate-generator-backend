package com.example.local_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.local_project.Entity.CertificateTemplates;

public interface  CertificateTemplatesRepo extends JpaRepository<CertificateTemplates, Long> {
    
}
