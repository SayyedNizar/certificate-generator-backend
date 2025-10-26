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

import com.example.local_project.Entity.Institutions;
import com.example.local_project.Services.InstitutionService;
import com.example.local_project.dto.InstitutionDto;

@RestController
@RequestMapping("/api/institutions") // Use a consistent base path for all institution-related endpoints
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService; // Renamed for consistency

    /**
     * Fetches a paginated list of all institutions.
     * Accessible only to ADMINs and INSTRUCTORs for populating dropdowns.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('INSTRUCTOR')")
    public Page<InstitutionDto> fetchInstitutions(Pageable pageable) {
        return institutionService.getAllInstitutions(pageable);
    }
    
    /**
     * Creates a new institution.
     * Accessible only to ADMINs.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Institutions postInstitutions(@RequestBody Institutions inst) {
        return institutionService.saveInstitutions(inst);
    }

    /**
     * Deletes an institution by its ID.
     * Accessible only to ADMINs.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete_Institutions(@PathVariable Long id) {
        return institutionService.deleteInstitutions(id);
    }

    /**
     * Updates an existing institution's name.
     * Accessible only to ADMINs.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Institutions putInstitutions(@PathVariable Long id, @RequestBody Institutions ins) {
        return institutionService.updateInstitutions(id, ins);
    }
}
