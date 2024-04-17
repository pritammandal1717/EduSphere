package org.education.controller;

import org.education.entity.Course;
import org.education.entity.Student;
import org.education.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CourseController {
    private  final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/coursesList")
    public String openProductsListPage(Model model)
    {
        List<Course> list_courses = courseService.getAllCoursesListService();
        model.addAttribute("model_courses_list", list_courses);
        return "courses-list";
    }

    @GetMapping("/courseDetails")
    public String openCourseDetailsPage(@RequestParam("courseName") String coursename, Model model)
    {
        Course course = courseService.getCourseDetailsService(coursename);
        model.addAttribute("model_course", course);

        return "course-details";
    }

    @GetMapping("/courses/{coursetype}")
    public String findCourseByType(@PathVariable String coursetype, Model model, @ModelAttribute("loggedInStudent") Student loggedInStudent) {
        List<Course> list_courses_type = courseService.getAllCourseByType(coursetype);
        model.addAttribute("model_courses_list_type", list_courses_type);
        model.addAttribute("student", loggedInStudent);
        return "course-list-by-type";
    }

    @GetMapping("/course/{coursedegree}")
    public String findCourseByDegree(@PathVariable String coursedegree, Model model) {
        List<Course> list_courses_degree = courseService.getAllCourseByDegree(coursedegree);
        model.addAttribute("model_courses_list_degree", list_courses_degree);
        return "course-list-by-degree";
    }
}
