package com.example.local_project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.local_project.Entity.CertificateTemplates;
import com.example.local_project.Services.CertificateTemplateServices;
import com.example.local_project.dto.TemplateDto;

@RestController
@RequestMapping("/api/templates") // 1. Consistent base path
public class CertificateTemplateController {

    @Autowired
    private CertificateTemplateServices templateService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('INSTRUCTOR')")
    public Page<TemplateDto> fetchTemplates(Pageable pageable) {
        return templateService.getAllTemplates(pageable);
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CertificateTemplates saveTemplate(@RequestBody CertificateTemplates template) {
        return templateService.saveTemplate(template);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteTemplate(@PathVariable Long id) {
        return templateService.deleteTemplate(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CertificateTemplates updateTemplate(@PathVariable Long id, @RequestBody CertificateTemplates template) {
        return templateService.UpdateCertificateTemplates(id, template);
    }
}
