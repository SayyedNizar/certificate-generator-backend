package com.example.local_project.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends a certificate notification email to a student.
     * @param toEmail The student's email address.
     * @param studentName The student's name.
     * @param courseName The name of the course they completed.
     * @param certificateId The ID of the certificate to create a download link.
     */
    public void sendCertificateNotification(String toEmail, String studentName, String courseName, Long certificateId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart message

            helper.setTo(toEmail);
            helper.setSubject("Congratulations! You've earned a new certificate!");
            
            // Build the email body with HTML for a nice link
            String frontendUrl = "http://localhost:5173"; // Your frontend's URL
            String downloadLink = frontendUrl + "/student-dashboard"; // Direct link to their dashboard
            
            String htmlBody = "<html>"
                            + "<body>"
                            + "<h3>Congratulations, " + studentName + "!</h3>"
                            + "<p>You have successfully completed the course: <strong>" + courseName + "</strong>.</p>"
                            + "<p>Your new certificate is now available in your dashboard. Click the link below to view and download it.</p>"
                            + "<br>"
                            + "<a href=\"" + downloadLink + "\" "
                            + "   style=\"background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;\""
                            + ">View My Dashboard</a>"
                            + "<br><br>"
                            + "<p>Certificate ID: " + certificateId + "</p>"
                            + "</body>"
                            + "</html>";

            helper.setText(htmlBody, true); // true = this is HTML
            
            mailSender.send(message);
            
        } catch (MessagingException e) {
            // In a real app, you'd have more robust error logging here
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
}
