package org.education.service;

import lombok.RequiredArgsConstructor;
import org.education.entity.PasswordResetToken;
import org.education.entity.Student;
import org.education.repository.PasswordResetTokenRepository;
import org.education.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl  implements PasswordResetTokenService{
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public String validatePasswordResetToken(String theToken) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(theToken);
        if (passwordResetToken.isEmpty()){
            return "invalid";
        }
        Calendar calendar = Calendar.getInstance();
        if ((passwordResetToken.get().getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<Student> findStudentByPasswordResetToken(String theToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(theToken).get().getStudent());
    }

    @Override
    public void resetPassword(Student student, String newPassword) {
        student.setPassword(passwordEncoder.encode(newPassword));
        studentRepository.save(student);
    }
    @Override
    public void createPasswordResetTokenForStudent(Student student, String passwordResetToken) {
        PasswordResetToken resetToken = new PasswordResetToken(passwordResetToken, student);
        passwordResetTokenRepository.save(resetToken);
    }
}
