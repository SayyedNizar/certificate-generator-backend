package com.example.local_project.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "certificate_templates")
@Getter
@Setter
@Data
public class CertificateTemplates {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Template_id")
    private Long templateId;
    @Column(name = "Template_name")
    private String templateName;
    
}
