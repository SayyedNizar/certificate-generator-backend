// package com.example.local_project.Services;

package com.example.local_project.Services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.local_project.Entity.Certificates;
import com.example.local_project.Repository.CertificateRepo;
import com.example.local_project.dto.CertificateDto;
import com.example.local_project.dto.CourseDto;
import com.example.local_project.dto.UserDto;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

@Service
public class CertificateService {

    @Autowired
    CertificateRepo certificateRepo;

    // Updated to return a Page of DTOs for consistency and to prevent lazy loading issues.
    public Page<CertificateDto> getAllCertificates(Pageable pageable) {
        Page<Certificates> certificatePage = certificateRepo.findAll(pageable);
        return certificatePage.map(this::convertToDto);
    }

    public Certificates saveCertificate(Certificates certificate) {
        return certificateRepo.save(certificate);
    }

    public String deleteCertificate(Long id) {
        if (certificateRepo.existsById(id)) {
            certificateRepo.deleteById(id);
            return "Certificate Deleted Successfully";
        }
        return "Certificate Not Found";
    }

    public Certificates updateCertificate(Long id, Certificates certificate) {
        Certificates obj = certificateRepo.findById(id).orElseThrow(() -> new RuntimeException("Certificate not found with id: " + id));

        obj.setCertificateNumber(certificate.getCertificateNumber());
        obj.setDateOfIssue(certificate.getDateOfIssue());
        return certificateRepo.save(obj);
    }

    // This is the single, correct version of the method that returns a Page of DTOs.
    public Page<CertificateDto> findCertificatesByUserEmail(String email, Pageable pageable) {
        Page<Certificates> certificatePage = certificateRepo.findByUserEmail(email, pageable);
        return certificatePage.map(this::convertToDto);
    }

    public byte[] generateCertificatePdf(Long certificateId) throws IOException {
        Certificates certificate = certificateRepo.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found with id: " + certificateId));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4.rotate());

        InputStream is = new ClassPathResource("certificate_template.png").getInputStream();
        byte[] imageBytes = is.readAllBytes();
        Image backgroundImage = new Image(ImageDataFactory.create(imageBytes));
        backgroundImage.setFixedPosition(0, 0);
        backgroundImage.scaleToFit(pdfDoc.getDefaultPageSize().getWidth(), pdfDoc.getDefaultPageSize().getHeight());
        document.add(backgroundImage);

        Paragraph studentName = new Paragraph(certificate.getUser().getName())
                .setFontSize(48)
                .setTextAlignment(TextAlignment.CENTER)
                .setFixedPosition(0, 290, pdfDoc.getDefaultPageSize().getWidth());

        Paragraph courseName = new Paragraph(certificate.getCourse().getCourseName())
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFixedPosition(0, 195, pdfDoc.getDefaultPageSize().getWidth());

        Paragraph issueDate = new Paragraph("Issued on: " + certificate.getDateOfIssue().toString())
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setFixedPosition(0, 80, pdfDoc.getDefaultPageSize().getWidth());

        document.add(studentName);
        document.add(courseName);
        document.add(issueDate);

        document.close();
        return baos.toByteArray();
    }

    public boolean isOwner(Long certificateId, String email) {
        Certificates certificate = certificateRepo.findById(certificateId).orElse(null);
        if (certificate == null || certificate.getUser() == null) {
            return false;
        }
        return certificate.getUser().getEmail().equalsIgnoreCase(email);
    }

    // This private helper method is now used by both getAllCertificates and findCertificatesByUserEmail.
    private CertificateDto convertToDto(Certificates certificate) {
        CertificateDto dto = new CertificateDto();
        dto.setCertificateId(certificate.getCertificateId());
        dto.setCertificateNumber(certificate.getCertificateNumber());
        dto.setDateOfIssue(certificate.getDateOfIssue());

        UserDto userDto = new UserDto();
        userDto.setId(certificate.getUser().getId());
        userDto.setName(certificate.getUser().getName());
        userDto.setEmail(certificate.getUser().getEmail());
        dto.setUser(userDto);

        CourseDto courseDto = new CourseDto();
        courseDto.setCourseId(certificate.getCourse().getCourseId());
        courseDto.setCourseName(certificate.getCourse().getCourseName());
        dto.setCourse(courseDto);

        return dto;
    }
}
