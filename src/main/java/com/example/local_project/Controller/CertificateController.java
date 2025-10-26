// package com.example.local_project.Controller;

package com.example.local_project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.local_project.Entity.Certificates;
import com.example.local_project.Services.CertificateService;
import com.example.local_project.dto.CertificateDto;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    // Corrected return type
    public Page<CertificateDto> getAllCertificates(Pageable pageable) {
        return certificateService.getAllCertificates(pageable);
    }

    @GetMapping("/my-certificates")
    @PreAuthorize("hasAuthority('STUDENT')")
    // Corrected return type
    public Page<CertificateDto> getMyCertificates(Authentication authentication, Pageable pageable) {
        String userEmail = authentication.getName();
        return certificateService.findCertificatesByUserEmail(userEmail, pageable);
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('INSTRUCTOR')")
    public Certificates postCertificate(@RequestBody Certificates certificate) {
        return certificateService.saveCertificate(certificate);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('INSTRUCTOR')")
    public String removeCertificates(@PathVariable Long id) {
        return certificateService.deleteCertificate(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('INSTRUCTOR')")
    public Certificates putCertificate(@PathVariable Long id, @RequestBody Certificates certificate) {
        return certificateService.updateCertificate(id, certificate);
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAuthority('ADMIN') or @certificateService.isOwner(#id, principal.username)")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long id) {
        try {
            byte[] pdfBytes = certificateService.generateCertificatePdf(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "certificate-" + id + ".pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
