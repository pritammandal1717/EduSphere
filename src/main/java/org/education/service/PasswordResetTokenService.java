package org.education.service;

import org.education.entity.Student;

import java.util.Optional;

public interface PasswordResetTokenService {
    String validatePasswordResetToken(String theToken);

    Optional<Student> findStudentByPasswordResetToken(String theToken);

    void resetPassword(Student student, String password);

    void createPasswordResetTokenForStudent(Student student, String passwordResetToken);
}
