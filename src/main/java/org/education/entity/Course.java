package org.education.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.education.urls.OtherUrls;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Entity
@Table(name = "course")
@Setter
@Getter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String coursename;

    @Column(nullable = false)
    private String coursedetails;

    @Column(nullable = false)
    private String coursedegree;
    @Column(nullable = false)
    private String coursetype;

    @Column(nullable = false)
    private String syllabus;

    @Column(nullable = false)
    private String originalprice;

    @Column(nullable = false)
    private String discountedprice;

    @Column(nullable = false)
    private String coursevalidity;

    @Column(nullable = false)
    private String courseimage;

    @Column(nullable = false)
    private String trainersname;

    @Column(nullable = false)
    private String trainersdetails;

    @Column(nullable = false)
    private String trainersimage;

    @Column(nullable = false)
    private String otherdetails;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(name = "course_student_mapping", joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))

    private Set<Student> students;


    public void setCourseimage(MultipartFile file) {
        this.courseimage = OtherUrls.IMAGE_UPLOAD_URL + file.getOriginalFilename();
    }
    public void setTrainersimage(MultipartFile file) {
        this.trainersimage = OtherUrls.IMAGE_UPLOAD_URL + file.getOriginalFilename();
    }
}
