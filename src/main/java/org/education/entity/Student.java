package org.education.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.education.urls.OtherUrls;
import org.hibernate.annotations.NaturalId;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId(mutable = true)
    private String email;
    private String password;
    private boolean isEnabled = false;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "student_roles",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    private String phoneno;

    private String currentdegree;

    private String profileimage;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "course_student_mapping", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses;
    public Student(String firstName, String lastName, String email,
                String password, Collection<Role> roles,String phoneno, String currentdegree, String profileimage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.phoneno = phoneno;
        this.currentdegree = currentdegree;
        this.profileimage = profileimage;
    }
    public void setProfileimage(MultipartFile file) {
        this.profileimage = OtherUrls.IMAGE_UPLOAD_URL + file.getOriginalFilename();
    }
}
