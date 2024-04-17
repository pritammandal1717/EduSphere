package org.education.controller;

import org.education.entity.Course;
import org.education.service.CourseService;
import org.education.urls.OtherUrls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private  final CourseService courseService;

    public AdminController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/addCourses")
    public String openAddCoursesPage(Model model)
    {
        model.addAttribute("courseAttr", new Course());
        return "add-courses";
    }

    @PostMapping("/addCourseForm")
    public String addCourseDetails(
            @ModelAttribute("courseAttr") Course course,
            @RequestParam("courseimage") MultipartFile courseImage,
            @RequestParam("trainersimage") MultipartFile trainerImage,
            RedirectAttributes redirectAttributes
    )
    {
        saveImage(courseImage);
        saveImage(trainerImage);

        boolean status = courseService.addCourseService(course);
        if(status)
        {
            redirectAttributes.addAttribute("redirect_attr_success", "Course added successfully");
        }
        else
        {
            redirectAttributes.addAttribute("redirect_attr_error", "Course not added due to some error");
        }

        return "redirect:/addCourses";
    }

    private boolean saveImage(MultipartFile file)
    {
        boolean status = false;

        try
        {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(OtherUrls.IMAGE_UPLOAD_FOLDER, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            status = true;
        }
        catch(Exception e)
        {
            status = false;
            e.printStackTrace();
        }

        return status;
    }
    @GetMapping("/coursesList")
    public String openCourseListPage(Model model)
    {
        List<Course> list_courses = courseService.getAllCoursesListService();
        model.addAttribute("model_courses_list", list_courses);
        return "courses-list";
    }

    @GetMapping("/courseDetails")
    public String openCourseDetailsPage(@PathVariable("courseName") String coursename, Model model)
    {
        Course course = courseService.getCourseDetailsService(coursename);
        model.addAttribute("model_course", course);

        return "course-details";
    }
}
