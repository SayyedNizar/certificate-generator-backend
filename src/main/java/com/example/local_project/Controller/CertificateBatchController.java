package com.example.local_project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.local_project.Entity.CertificateBatch;
import com.example.local_project.Services.CertificateBatchService;


@RestController
@RequestMapping("/api/batches")
public class CertificateBatchController {

    @Autowired CertificateBatchService batchService;

    @PostMapping("/generate-bulk")
     @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> generateBulkCertificates(
         @RequestParam("file") MultipartFile file,
            @RequestParam("courseId") Long courseId,
            @RequestParam("templateId") Long templateId,
            @RequestParam("institutionId") Long institutionId,
            @RequestParam("requestedByUserId") Long requestedByUserId)
    {
        try {
            CertificateBatch batch = batchService.startBatchProcess(
                file, courseId, templateId, institutionId, requestedByUserId
            );
            return ResponseEntity.ok("Batch job started successfully with ID: " + batch.getBatchId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not start batch job: " + e.getMessage());
        }
    }

    
}
