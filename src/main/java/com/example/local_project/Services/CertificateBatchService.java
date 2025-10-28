package com.example.local_project.Services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.local_project.Entity.BatchStatus;
import com.example.local_project.Entity.CertificateBatch;
import com.example.local_project.Entity.CertificateTemplates;
import com.example.local_project.Entity.Certificates;
import com.example.local_project.Entity.Courses;
import com.example.local_project.Entity.Institutions;
import com.example.local_project.Entity.Users;
import com.example.local_project.Repository.CertificateBatchRepo;
import com.example.local_project.Repository.CertificateRepo;
import com.example.local_project.Repository.CertificateTemplatesRepo;
import com.example.local_project.Repository.CourseRepo;
import com.example.local_project.Repository.InstitutionsRepo;
import com.example.local_project.Repository.UsersRepo;

@Service
public class CertificateBatchService {

    @Autowired private CertificateBatchRepo batchRepo;
    @Autowired private UsersRepo userRepo;
    @Autowired private CertificateRepo certificateRepo;
    @Autowired private CourseRepo courseRepo;
    @Autowired private CertificateTemplatesRepo templateRepo;
    @Autowired private InstitutionsRepo institutionRepo;
    
    @Autowired private EmailService emailService; 

    @Transactional
    public CertificateBatch startBatchProcess(MultipartFile file, Long courseId, Long templateId, Long institutionId, Long requestedByUserId) {
        Courses course = courseRepo.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        CertificateTemplates template = templateRepo.findById(templateId).orElseThrow(() -> new RuntimeException("Template not found"));
        Institutions institution = institutionRepo.findById(institutionId).orElseThrow(() -> new RuntimeException("Institution not found"));
        Users requestedBy = userRepo.findById(requestedByUserId).orElseThrow(() -> new RuntimeException("Requesting user not found"));

        CertificateBatch batch = new CertificateBatch();
        batch.setCourse(course);
        batch.setTemplate(template);
        batch.setInstitution(institution);
        batch.setRequestedBy(requestedBy);
        batch.setStatus(BatchStatus.PENDING);
        batch.setRequestTimestamp(LocalDateTime.now());
        CertificateBatch savedBatch = batchRepo.save(batch);

        processFileInBackground(file, savedBatch.getBatchId());

        return savedBatch;
    }

    @Async
    @Transactional
    public void processFileInBackground(MultipartFile file, Long batchId) { 
        
        CertificateBatch batch = batchRepo.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        batch.setStatus(BatchStatus.PROCESSING);
        batchRepo.save(batch);

        int successCount = 0;
        int failureCount = 0;

        Courses course = courseRepo.findById(batch.getCourse().getCourseId()).get();
        CertificateTemplates template = templateRepo.findById(batch.getTemplate().getTemplateId()).get();
        Institutions institution = institutionRepo.findById(batch.getInstitution().getInstitutionId()).get();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            for (CSVRecord csvRecord : csvParser) {
                String userEmail = csvRecord.get("email");
                Users user = userRepo.findByEmail(userEmail).orElse(null);

                if (user != null) {
                    Certificates newCert = new Certificates();
                    newCert.setUser(user);
                    newCert.setCourse(course);
                    newCert.setCertificateTemplate(template);
                    newCert.setInstitution(institution);
                    newCert.setDateOfIssue(LocalDate.now());
                    newCert.setCertificateNumber(UUID.randomUUID().toString());
                    
                    Certificates savedCert = certificateRepo.save(newCert);
                    successCount++;

                    // --- THIS IS THE FIX ---
                    // We wrap the email sending in its own try-catch block.
                    // If one email fails, it will print an error but will NOT crash the whole batch.
                    try {
                        emailService.sendCertificateNotification(
                            user.getEmail(),
                            user.getName(),
                            course.getCourseName(),
                            savedCert.getCertificateId()
                        );
                    } catch (Exception emailException) {
                        System.err.println("EMAIL FAILED for user " + user.getEmail() + ": " + emailException.getMessage());
                        // We don't increment failureCount here because the certificate *was* created.
                    }
                    // -----------------------

                } else {
                    System.err.println("User not found for email: " + userEmail);
                    failureCount++;
                }
            }
        } catch (Exception e) {
            // This 'catch' will now only catch critical errors, like a bad CSV file.
            batch.setStatus(BatchStatus.FAILED);
            batchRepo.save(batch);
            e.printStackTrace();
            return;
        }

        // The batch will now complete successfully, even if some emails failed.
        batch.setStatus(BatchStatus.COMPLETED);
        batch.setCompletionTimestamp(LocalDateTime.now());
        batch.setSuccessfulRecords(successCount);
        batch.setFailedRecords(failureCount);
        batchRepo.save(batch);
    }
}

