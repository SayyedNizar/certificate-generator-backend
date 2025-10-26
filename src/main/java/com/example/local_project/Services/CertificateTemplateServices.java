package com.example.local_project.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.local_project.Entity.CertificateTemplates;
import com.example.local_project.Repository.CertificateTemplatesRepo;
import com.example.local_project.dto.TemplateDto;
@Service
public class CertificateTemplateServices {
    @Autowired CertificateTemplatesRepo certificateTemplatesRepo;

    public List<CertificateTemplates> getAllTemplates() {
        return certificateTemplatesRepo.findAll();
    }

    public CertificateTemplates saveTemplate(CertificateTemplates template) {
        return certificateTemplatesRepo.save(template);
    }

    public String deleteTemplate(Long id) {
       if(certificateTemplatesRepo.existsById(id)){
        certificateTemplatesRepo.deleteById(id);
        return "Template Deleted Successfully";
       }
         return "Template Not Found";
    }

    public CertificateTemplates UpdateCertificateTemplates(Long id, CertificateTemplates template) {
        CertificateTemplates obj = certificateTemplatesRepo.findById(id).orElse(null);
        
        if (obj != null) {
            obj.setTemplateName(template.getTemplateName());
            return certificateTemplatesRepo.save(obj);
        }

        return null;
    }

    public Page<TemplateDto> getAllTemplates(Pageable pageable) {
        Page<CertificateTemplates> templatePage = certificateTemplatesRepo.findAll(pageable);
        return templatePage.map(this::convertToDto);
    }

    private TemplateDto convertToDto(CertificateTemplates template) {
        TemplateDto dto = new TemplateDto();
        dto.setTemplateId(template.getTemplateId());
        dto.setTemplateName(template.getTemplateName());
        return dto;
    }
}
