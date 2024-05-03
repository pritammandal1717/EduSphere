package org.education.service;

import org.education.dto.RegistrationRequest;
import org.education.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getAllStudents();
    Student registerStudent(RegistrationRequest registrationRequest);
    Optional<Student> findByEmail(String email);

    Optional<Student> findById(Long id);

    void updateStudent(Long id, String firstName, String lastName, String email);

    void deleteStudent(Long id);

    void saveStudent(Student student);
}
