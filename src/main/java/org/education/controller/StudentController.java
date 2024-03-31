package org.education.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.education.entity.Student;
import org.education.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public String getStudents(Model model){
        model.addAttribute("users", studentService.getAllStudents());
        return "students";
    }
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
}
