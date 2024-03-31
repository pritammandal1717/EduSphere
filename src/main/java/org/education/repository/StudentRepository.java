package org.education.repository;

import org.education.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE Student s set s.firstName =:firstName,"+
            " s.lastName =:lastName," + "s.email =:email where s.id =:id")
    void update(String firstName, String lastName, String email, Long id);
}
