package com.example.local_project.dto;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CertificateDto {
    private Long certificateId;
    private String certificateNumber;
    private LocalDate dateOfIssue;
    private UserDto user;
    private CourseDto course;
    // We don't need to send template or institution details for this page
}

