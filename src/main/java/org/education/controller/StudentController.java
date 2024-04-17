package org.education.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.education.entity.Course;
import org.education.entity.Student;
import org.education.service.CourseService;
import org.education.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Struct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;
    @ModelAttribute("loggedInStudent")
    public void addLoggedInStudent(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUserName = authentication.getName();
            Optional<Student> studentOptional = studentService.findByEmail(currentUserName);
            studentOptional.ifPresent(student -> model.addAttribute("loggedInStudent", student));
        }
    }
        @GetMapping()
    public String openStudentProfile(Model model, @ModelAttribute("loggedInStudent") Student loggedInStudent){
        model.addAttribute("student", loggedInStudent);
        return "home-student";
    }

//    @GetMapping("/getAllStudents")
//    public String getStudents(Model model){
//        model.addAttribute("students", studentService.getAllStudents());
//        return "students";
//    }
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        Optional<Student> student = studentService.findById(id);
        model.addAttribute("student", student.get());
        return "update-student";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable("id") Long id, Student student){
        studentService.updateStudent(id, student.getFirstName(), student.getLastName(), student.getEmail());
        return "redirect:/students?update_success";
    }
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id){
        studentService.deleteStudent(id);
        return "redirect:/students?delete_success";
    }

    @GetMapping("/courses/{courseName}/purchase")
    public String showPurchaseCourseForm(@PathVariable("courseName") String courseName, Model model) {
        Course course = courseService.findCourseByName(courseName);
        if (course != null) {
            model.addAttribute("course", course);
            return "purchase-course";
        } else {
            return "redirect:/courses";
        }
    }
    @PostMapping("/courses/{courseName}/confirmPurchase")
    public String confirmPurchase(@PathVariable("courseName")String courseName, Model model, @ModelAttribute("loggedInStudent") Student loggedInStudent){
        Course course = courseService.getCourseDetailsService(courseName);
        Set<Course> courses = loggedInStudent.getCourses();
        if (courses == null) {
            courses = new HashSet<>();
            courses.add(course);
        }else {
            courses.add(course);
        }
        model.addAttribute("student", loggedInStudent);
        return "redirect:/students/home-student";
    }
}
