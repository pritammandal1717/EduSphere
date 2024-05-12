package org.education.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.education.entity.Course;
import org.education.entity.Student;
import org.education.model.ChatMessage;
import org.education.service.CourseService;
import org.education.service.StudentService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Struct;
import java.util.*;

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
    public String openStudentHome(Model model, @ModelAttribute("loggedInStudent") Student loggedInStudent){
        model.addAttribute("student", loggedInStudent);
            Set<Course> enrolledCourses = loggedInStudent.getCourses();
            model.addAttribute("enrolledCourses", enrolledCourses);
        return "home-student";
    }
    @GetMapping("/profile")
    public String openStudentProfile(Model model, @ModelAttribute("loggedInStudent") Student loggedInStudent){
        model.addAttribute("student", loggedInStudent);
        return "profile-student";
    }

    @GetMapping("/chat")
    public String openChatPage(){
        return "chat";
    }
    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage)
    {
        return chatMessage;
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

    @GetMapping("/courses")
    public String findCourseByType(Model model,@ModelAttribute("loggedInStudent") Student loggedInStudent) {
        List<Course> list_courses = courseService.getAllCoursesListService();
        model.addAttribute("courses_list", list_courses);
        model.addAttribute("student", loggedInStudent);
        return "course-list";
    }
    @GetMapping("/courses/purchase/{id}")
    public String showPurchaseCourseForm(@PathVariable("id") Integer id, Model model, @ModelAttribute("loggedInStudent") Student loggedInStudent) {
        model.addAttribute("student", loggedInStudent);
        Course course = courseService.getCourseById(id);
        if (course != null) {
            model.addAttribute("course", course);
            return "purchase-course";
        } else {
            return "course-list";
        }
    }
    @PostMapping("/courses/{courseid}/purchase")
    public String confirmPurchase(@PathVariable("courseid")Integer id, Model model, @ModelAttribute("loggedInStudent") Student loggedInStudent){
        model.addAttribute("student", loggedInStudent);
        Course course = courseService.getCourseById(id);
        loggedInStudent.getCourses().add(course);
        studentService.saveStudent(loggedInStudent);
        return "successful-purchase";
    }
    @GetMapping("/exams")
    public String openExamPage(Model model, @ModelAttribute("loggedInStudent") Student loggedInStudent){
        model.addAttribute("student", loggedInStudent);
        return "under-development";
    }
    @GetMapping("/quizes")
    public String openQuizPage(Model model, @ModelAttribute("loggedInStudent") Student loggedInStudent){
        model.addAttribute("student", loggedInStudent);
        return "under-development";
    }

}
