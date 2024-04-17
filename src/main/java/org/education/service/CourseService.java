package org.education.service;

import org.education.entity.Course;

import java.util.List;

public interface CourseService {
    public boolean addCourseService(Course course);
    public List<Course> getAllCoursesListService();
    public Course getCourseDetailsService(String coursename);
    public List<String> getAllCourseNameService();
    public Course getSelectedCourseDetailsService(String coursename);
    public List<Course> getAllCourseByType(String coursetype);
    public List<Course> getAllCourseByDegree(String coursedegree);
    public Course getCourseById(int id);
    public Course findCourseByName(String courseName);
}
