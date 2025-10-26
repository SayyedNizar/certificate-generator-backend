package com.example.local_project.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.local_project.Entity.Institutions;
import com.example.local_project.Repository.InstitutionsRepo;
import com.example.local_project.dto.InstitutionDto;

@Service
public class InstitutionService { // Renamed from InstitutionsService for convention

    @Autowired
    InstitutionsRepo institutionsRepo;

    // This is the only method needed to get institutions, and it supports pagination.
    public Page<InstitutionDto> getAllInstitutions(Pageable pageable) {
        Page<Institutions> institutionPage = institutionsRepo.findAll(pageable);
        return institutionPage.map(this::convertToDto);
    }

    public Institutions saveInstitutions(Institutions institution) {
        return institutionsRepo.save(institution);
    }
    
    public String deleteInstitutions(Long id) {
        if (institutionsRepo.existsById(id)) {
            institutionsRepo.deleteById(id);
            return "Value Deleted Successfully";
        } else {
            return "Id Not Found: " + id;
        }
    }

    public Institutions updateInstitutions(Long id, Institutions ins) {
        Institutions it = institutionsRepo.findById(id).orElse(null);
        if (it != null) {
            it.setInstitutionName(ins.getInstitutionName());
            return institutionsRepo.save(it);
        }
        return null; 
    }

    private InstitutionDto convertToDto(Institutions institution) {
        InstitutionDto dto = new InstitutionDto();
        dto.setInstitutionId(institution.getInstitutionId());
        // --- THIS IS THE FIX ---
        // The field is named 'institutionName', so the getter is 'getInstitutionName()'.
        dto.setName(institution.getInstitutionName());
        // -----------------------
        return dto;
    }
}
