package org.education.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.education.dto.RegistrationRequest;
import org.education.entity.Role;
import org.education.entity.Student;
import org.education.repository.StudentRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student registerStudent(RegistrationRequest registrationRequest) {
        String imageURL =  generateImageUrl(registrationRequest.getProfileimage());
        var student = new Student(registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                Arrays.asList(new Role("ROLE_USER")),
                registrationRequest.getPhoneno(),
                registrationRequest.getCurrentdegree(),
                imageURL);
        return studentRepository.save(student);
    }
    private String generateImageUrl(MultipartFile profileimage) {
        String baseUrl = "http://localhost:8080/uploads/";
        String filename = StringUtils.cleanPath(Objects.requireNonNull(profileimage.getOriginalFilename()));
        return baseUrl + filename;
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return Optional.ofNullable(studentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found")));
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateStudent(Long id, String firstName, String lastName, String email) {
        studentRepository.update(firstName,lastName,email,id);
    }

    @Override
    public void deleteStudent(Long id) {
        Optional<Student> theUser = studentRepository.findById(id);
        theUser.ifPresent(user -> verificationTokenService.deleteStudentToken(user.getId()));
        studentRepository.deleteById(id);
    }

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }
}
