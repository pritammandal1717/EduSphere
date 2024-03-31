package org.education.service;

import lombok.RequiredArgsConstructor;
import org.education.entity.Student;
import org.education.entity.VerificationToken;
import org.education.repository.StudentRepository;
import org.education.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService{

    private final VerificationTokenRepository tokenRepository;
    private final StudentRepository studentRepository;
    @Override
    public String validateToken(String token) {
        Optional<VerificationToken> theToken = tokenRepository.findByToken(token);
        if (theToken.isEmpty()){
            return "INVALID";
        }
        Student student = theToken.get().getStudent();
        Calendar calendar = Calendar.getInstance();
        if ((theToken.get().getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "EXPIRED";
        }
        student.setEnabled(true);
        studentRepository.save(student);
        return "VALID";
    }

    @Override
    public void saveVerificationTokenForStudent(Student student, String token) {
        var verificationToken = new VerificationToken(token, student);
        tokenRepository.save(verificationToken);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteStudentToken(Long id) {
        tokenRepository.deleteByStudentId(id);
    }
}
