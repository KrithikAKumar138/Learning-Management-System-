package com.LMS.controller;

import com.LMS.entity.Course;
import com.LMS.entity.Enrollment;
import com.LMS.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/courses")
    public String myCourses(
            @RequestParam(value = "filter", defaultValue = "All") String filter,
            Authentication authentication,
            Model model) {

        List<Enrollment> enrollments =
                studentService.getFreshEnrollmentsForStudent(authentication, filter);

        List<Course> availableCourses = studentService.getAvailableCourses();

        int totalCourses = enrollments.size();
        int inProgress = (int) enrollments.stream()
                .filter(e -> e.getStatus().equals("In Progress")).count();
        int completed = (int) enrollments.stream()
                .filter(e -> e.getStatus().equals("Completed")).count();
        int notStarted = (int) enrollments.stream()
                .filter(e -> e.getStatus().equals("Not Started")).count();

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("inProgress", inProgress);
        model.addAttribute("completed", completed);
        model.addAttribute("notStarted", notStarted);
        model.addAttribute("currentFilter", filter);

        return "student-courses";
    }

    @PostMapping("/enroll/{courseId}")
    public String enrollInCourse(@PathVariable Long courseId,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        try {
            studentService.enrollStudent(courseId, authentication);
            redirectAttributes.addFlashAttribute("message", "Successfully enrolled!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/student/courses";
    }

    @PostMapping("/progress/{enrollmentId}")
    @ResponseBody
    public String updateProgress(
            @PathVariable Long enrollmentId,
            @RequestParam double progress) {

        studentService.updateProgress(enrollmentId, progress);
        return "Progress updated to " + progress + "%";
    }

    @PostMapping("/complete/{enrollmentId}")
    @ResponseBody
    public String markAsCompleted(@PathVariable Long enrollmentId) {
        studentService.updateProgress(enrollmentId, 100);
        return "Course marked as completed";
    }
}

