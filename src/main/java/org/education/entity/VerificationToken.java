package org.education.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.education.utility.TokenExpirationTime;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public VerificationToken(String token, Student student) {
        this.token = token;
        this.student = student;
        this.expirationTime = TokenExpirationTime.getExpirationTime();
    }
}
