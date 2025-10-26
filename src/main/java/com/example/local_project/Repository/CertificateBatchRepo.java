package com.example.local_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.local_project.Entity.CertificateBatch;

@Repository
public interface CertificateBatchRepo extends JpaRepository<CertificateBatch, Long> {
    @Modifying
    @Query("DELETE FROM CertificateBatch b WHERE b.requestedBy.id = :userId")
    void deleteByRequestedById(@Param("userId") Long userId);
}