package com.example.local_project.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.local_project.Entity.Certificates;

public interface CertificateRepo extends JpaRepository<Certificates, Long> {

     
    Optional<Certificates> findByCertificateNumber(String certificateNumber);

 @Query("SELECT c FROM Certificates c JOIN c.user u WHERE u.email = :email")
    Page<Certificates> findByUserEmail(@Param("email") String email, Pageable pageable);

     @Modifying
    @Query("DELETE FROM Certificates c WHERE c.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}

