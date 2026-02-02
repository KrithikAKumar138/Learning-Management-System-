package com.LMS.controller;

import com.LMS.entity.Course;
import com.LMS.entity.Lesson;
import com.LMS.entity.QuizQuestion;
import com.LMS.exception.ResourceNotFoundException;
import com.LMS.repository.CourseRepository;
import com.LMS.repository.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InstructorPageController {

    private final CourseRepository courseRepository;



    public InstructorPageController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @GetMapping("/instructor/dashboard")
    public String instructorDashboard(Authentication authentication, Model model) {

        String email = authentication.getName();
        model.addAttribute("courses",
                courseRepository.findByCreatedBy(email));
        model.addAttribute("course", new Course());
        model.addAttribute("quizQuestion", new QuizQuestion());
        model.addAttribute("lesson", new Lesson());

        return "instructor-dashboard";
    }
    @GetMapping("/instructor/course/{courseId}")
    public String instructorCoursePlayer(
            @PathVariable Long courseId,
            @RequestParam(required = false) Long lessonId,
            @RequestParam(required = false) String view,
            Model model,
            Authentication authentication
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->  new ResourceNotFoundException("Course not found"));

        String email = authentication.getName();
        if (!course.getCreatedBy().equals(email)) {
            return "redirect:/instructor/dashboard";
        }

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

        var quizQuestions = quizQuestionRepository.findByCourseId(courseId);

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("currentLesson", currentLesson);
        model.addAttribute("quizQuestions", quizQuestions);

        model.addAttribute("view", view != null ? view : "lesson");
        model.addAttribute("basePath", "/instructor");

        return "instructor-course-player";
    }
}
