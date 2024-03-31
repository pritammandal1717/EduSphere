package org.education.event;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.education.entity.Student;
import org.education.service.VerificationTokenService;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final VerificationTokenService tokenService;
    private final JavaMailSender mailSender;
    private Student student;

    public RegistrationCompleteEventListener(VerificationTokenService tokenService, JavaMailSender mailSender) {
        this.tokenService = tokenService;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //1. get the user
        student = event.getStudent();
        //2. generate a token for the user
        String vToken = UUID.randomUUID().toString();
        //3. save the token for the user
        tokenService.saveVerificationTokenForStudent(student, vToken);
        //4. Build the verification url
        String url = event.getConfirmationUrl()+"/registration/verifyEmail?token="+vToken;
        //5. send the email to the user
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Students Verification Service";
        String mailContent = "<p> Hi, "+ student.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Students Registration Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, student);
    }


    public void sendPasswordResetVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Request Verification";
        String senderName = "Students Verification Service";
        String mailContent = "<p> Hi, "+ student.getFirstName()+ ", </p>"+
                "<p><b>You recently requested to reset your password,</b>"+"" +
                "Please, follow the link below to complete the action.</p>"+
                "<a href=\"" +url+ "\">Reset password</a>"+
                "<p> Students Registration Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, student);
    }

    private static void emailMessage(String subject, String senderName,
                                     String mailContent, JavaMailSender mailSender, Student student)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("dailycodeworks@gmail.com", senderName);
        messageHelper.setTo(student.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
