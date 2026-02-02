package com.LMS.controller;

import com.LMS.entity.Enrollment;
import com.LMS.entity.Lesson;
import com.LMS.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.LMS.entity.Course;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/student")
public class StudentCourseController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/learn/{courseId}")
    public String startLearning(
            @PathVariable Long courseId,
            Authentication authentication) {

        Enrollment enrollment =
                studentService.getEnrollmentByStudentEmailAndCourseId(
                        authentication.getName(), courseId);

        if (enrollment == null) {
            return "redirect:/student/courses";
        }

        List<Lesson> lessons =
                studentService.getLessonsByCourse(courseId);

        if (lessons == null || lessons.isEmpty()) {
            return "redirect:/student/courses";
        }

        return "redirect:/student/course/" + courseId
                + "?lessonId=" + lessons.get(0).getId();
    }
    @GetMapping("/course/{courseId}")
    public String viewCoursePlayer(
            @PathVariable Long courseId,
            @RequestParam(required = false) Long lessonId,
            Authentication authentication,
            Model model) {

        Enrollment enrollment = studentService
                .getEnrollmentByStudentEmailAndCourseId(
                        authentication.getName(), courseId);

        if (enrollment == null) {
            return "redirect:/student/courses";
        }

        Course course = enrollment.getCourse();
        List<Lesson> lessons = studentService.getLessonsByCourse(courseId);

        Lesson currentLesson = (lessonId == null)
                ? lessons.get(0)
                : lessons.stream()
                .filter(l -> l.getId().equals(lessonId))
                .findFirst()
                .orElse(lessons.get(0));
        Lesson lastLesson = lessons.get(lessons.size() - 1);
        boolean isLastLesson = currentLesson.getId().equals(lastLesson.getId());

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("currentLesson", currentLesson);
        model.addAttribute("enrollment", enrollment);

        model.addAttribute("isLastLesson", isLastLesson);

        return "student-course-player";
    }

    @PostMapping("/lesson/complete/{lessonId}")
    public String completeLesson(
            @PathVariable Long lessonId,
            @RequestParam Long courseId,
            Authentication authentication) {

        Enrollment enrollment = studentService
                .getEnrollmentByStudentEmailAndCourseId(
                        authentication.getName(), courseId);

        if (enrollment == null) {
            return "redirect:/student/courses";
        }

        studentService.markLessonCompleted(enrollment.getId(), lessonId);

        return "redirect:/student/course/" + courseId + "?lessonId=" + lessonId;
    }






}


