package com.LMS.controller;
import com.LMS.entity.Course;
import com.LMS.exception.ResourceNotFoundException;
import com.LMS.repository.CourseRepository;
import com.LMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.LMS.repository.QuizQuestionRepository;
import com.LMS.entity.QuizQuestion;

import java.util.List;

@Controller
public class AdminPageController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;


    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {

        List<Course> allCourses = courseRepository.findAll();
        model.addAttribute("courses", allCourses);


        long totalCourses = allCourses.size();
        long pendingCourses = allCourses.stream()
                .filter(course -> !course.isApproved())
                .count();
        long approvedCourses = totalCourses - pendingCourses;



        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("pendingCourses", pendingCourses);
        model.addAttribute("approvedCourses", approvedCourses);

        return "admin-dashboard";
    }
    @PostMapping("/admin/course/{id}/approve")
    @ResponseBody
    public String approveCourse(@PathVariable Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        course.setApproved(true);
        courseRepository.save(course);

        return "Course approved";
    }
    @GetMapping("/admin/course/{courseId}")
    public String adminCoursePlayer(
            @PathVariable Long courseId,
            @RequestParam(required = false) Long lessonId,
            @RequestParam(required = false, defaultValue = "lesson") String view,
            Model model
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        var lessons = course.getLessons()
                .stream()
                .sorted((a, b) -> a.getOrderNumber().compareTo(b.getOrderNumber()))
                .toList();

        var currentLesson = lessons.isEmpty() ? null : lessons.get(0);

        if (lessonId != null) {
            currentLesson = lessons.stream()
                    .filter(l -> l.getId().equals(lessonId))
                    .findFirst()
                    .orElse(currentLesson);
        }

        List<QuizQuestion> quizQuestions =
                quizQuestionRepository.findByCourseId(courseId);

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("currentLesson", currentLesson);
        model.addAttribute("quizQuestions", quizQuestions);
        model.addAttribute("view", view);
        model.addAttribute("basePath", "/admin");


        return "admin-course-player";
    }

    @PostMapping("/admin/course/{id}/delete")
    @ResponseBody
    public String deleteCourseByAdmin(@PathVariable Long id) {
        courseRepository.deleteById(id);
        return "Course deleted successfully";
    }

}