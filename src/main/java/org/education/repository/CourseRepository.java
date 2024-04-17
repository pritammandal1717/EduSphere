package org.education.repository;

import org.education.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Course findByCoursename(String coursename);
    @Query("SELECT coursename FROM Course")
    List<String> findCourseName();
    List<Course> findByCoursetype(String coursetype);
    List<Course> findByCoursedegree(String coursedegree);
}
