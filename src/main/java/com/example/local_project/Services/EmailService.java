package com.example.local_project.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // 1. Import Value
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // 2. Inject the new variable from application.properties
    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    public void sendCertificateNotification(String toEmail, String studentName, String courseName, Long certificateId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Congratulations! You've earned a new certificate!");
            
            // 3. Use the injected variable to build the link
            String downloadLink = frontendUrl + "/student-dashboard"; // Direct link to their dashboard
            
            String htmlBody = "<html>"
                            + "<body>"
                            + "<h3>Congratulations, " + studentName + "!</h3>"
                            + "<p>You have successfully completed the course: <strong>" + courseName + "</strong>.</p>"
                            + "<p>Your new certificate is now available on your CertifyMe dashboard. Click the link below to log in and view it.</p>"
                            + "<br>"
                            + "<a href=\"" + downloadLink + "\" "
                            + "   style=\"background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;\""
                            + ">View My Dashboard</a>"
                            + "<br><br>"
                            + "<p>Certificate ID: " + certificateId + "</p>"
                            + "</body>"
                            + "</html>";

            helper.setText(htmlBody, true);
            
            mailSender.send(message);
            
        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
}

