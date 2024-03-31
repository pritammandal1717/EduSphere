package org.education.service;

import org.education.entity.Student;
import org.education.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {
    String validateToken(String token);
    void saveVerificationTokenForStudent(Student student, String token);
    Optional<VerificationToken> findByToken(String token);


    void deleteStudentToken(Long id);
}
