package org.education.service;

import org.education.entity.Course;
import org.education.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean addCourseService(Course course)
    {
        boolean status = false;

        try
        {
            courseRepository.save(course);
            status = true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }

    @Override
    public List<Course> getAllCoursesListService()
    {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseDetailsService(String coursename)
    {
        return courseRepository.findByCoursename(coursename);
    }

    @Override
    public List<String> getAllCourseNameService()
    {
        return courseRepository.findCourseName();
    }

    @Override
    public Course getSelectedCourseDetailsService(String coursename)
    {
        return courseRepository.findByCoursename(coursename);
    }

    @Override
    public List<Course> getAllCourseByType(String coursetype) {

        return courseRepository.findByCoursetype(coursetype);
    }

    @Override
    public List<Course> getAllCourseByDegree(String coursedegree) {
        return courseRepository.findByCoursedegree(coursedegree);
    }

    @Override
    public Course getCourseById(int id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Override
    public Course findCourseByName(String courseName) {
        return courseRepository.findByCoursename(courseName);
    }
}
