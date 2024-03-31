package org.education.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.education.dto.RegistrationRequest;
import org.education.entity.Student;
import org.education.entity.VerificationToken;
import org.education.event.RegistrationCompleteEvent;
import org.education.event.RegistrationCompleteEventListener;
import org.education.service.PasswordResetTokenService;
import org.education.service.StudentService;
import org.education.service.VerificationTokenService;
import org.education.utility.UrlUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class AuthenticationController {
    private final StudentService studentService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService tokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final RegistrationCompleteEventListener eventListener;

    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("student", new RegistrationRequest());
        return "registration";
    }
    @PostMapping("/register")
    public String registerStudent(@ModelAttribute("user") RegistrationRequest registration, HttpServletRequest request) {
        Student student = studentService.registerStudent(registration);
        publisher.publishEvent(new RegistrationCompleteEvent(student, UrlUtil.getApplicationUrl(request)));
        return "redirect:/registration/registration-form?success";
    }
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> theToken = tokenService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getStudent().isEnabled()) {
            return "redirect:/login?verified";
        }
        String verificationResult = tokenService.validateToken(token);
        switch (verificationResult.toLowerCase()) {
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }
    @GetMapping("/forgot-password-request")
    public String forgotPasswordForm(){
        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String resetPasswordRequest(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        Optional<Student> student= studentService.findByEmail(email);
        if (student.isEmpty()){
            return  "redirect:/registration/forgot-password-request?not_fond";
        }
        String passwordResetToken = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetTokenForStudent(student.get(), passwordResetToken);
        //send password reset verification email to the user
        String url = UrlUtil.getApplicationUrl(request)+"/registration/password-reset-form?token="+passwordResetToken;
        try {
            eventListener.sendPasswordResetVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/registration/forgot-password-request?success";
    }
    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model){
        model.addAttribute("token", token);
        return "password-reset-form";
    }
    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request){
        String theToken = request.getParameter("token");
        String password = request.getParameter("password");
        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(theToken);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")){
            return "redirect:/error?invalid_token";
        }
        Optional<Student> student = passwordResetTokenService.findStudentByPasswordResetToken(theToken);
        if (student.isPresent()){
            passwordResetTokenService.resetPassword(student.get(), password);
            return "redirect:/login?reset_success";
        }
        return "redirect:/error?not_found";
    }
}
