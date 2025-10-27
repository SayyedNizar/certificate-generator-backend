package com.example.local_project.Services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailService {

    // 1. Inject the SendGrid API Key from our environment variables
    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    // 2. Inject the "From" email (e.g., sayyednizar7@gmail.com)
    @Value("${MAIL_USER}")
    private String fromEmail;
    
    // 3. Inject the Vercel URL
    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    public void sendCertificateNotification(String toEmail, String studentName, String courseName, Long certificateId) {
        
        // The email we are sending from (must be your verified sender)
        Email from = new Email(fromEmail); 
        String subject = "Congratulations! You've earned a new certificate!";
        Email to = new Email(toEmail);
        
        String downloadLink = frontendUrl + "/student-dashboard";
        
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

        Content content = new Content("text/html", htmlBody);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            
            // Log the result to the console for debugging
            System.out.println("SendGrid response code for " + toEmail + ": " + response.getStatusCode());
            
        } catch (IOException ex) {
            System.err.println("Failed to send email via SendGrid to " + toEmail + ": " + ex.getMessage());
        }
    }
}

