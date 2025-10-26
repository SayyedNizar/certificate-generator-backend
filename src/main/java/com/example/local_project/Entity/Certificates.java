package com.example.local_project.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "certificates")
@Data
@Getter
@Setter
public class Certificates {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long certificateId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="receipient_id", nullable=false)
    private Users user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="course_id", nullable=false)
    private Courses course;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="template_id", nullable=false)
    private CertificateTemplates certificateTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    private Institutions institution;
    
    @Column(name="certificate_number", unique=true,nullable=false)
    private String certificateNumber;

    @Column(name="date_of_issue", nullable=false)
    private LocalDate dateOfIssue;
    
}
