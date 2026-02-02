package com.LMS.controller;

import com.LMS.entity.Course;
import com.LMS.entity.Lesson;
import com.LMS.entity.QuizQuestion;
import com.LMS.repository.CourseRepository;
import com.LMS.repository.QuizQuestionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

    @Controller
    @RequestMapping("/instructor/quiz")
    public class InstructorQuizController {

        @Autowired
        private CourseRepository courseRepository;

        @Autowired
        private QuizQuestionRepository quizQuestionRepository;

        @GetMapping("/{courseId}")
        public String quizForm(@PathVariable Long courseId, Model model) {

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            model.addAttribute("course", course);
            model.addAttribute("quizQuestion", new QuizQuestion()); // 🔥 REQUIRED

            return "instructor-quiz";
        }


        @PostMapping("/save")
        public String saveQuizQuestion(
                @Valid @ModelAttribute("quizQuestion") QuizQuestion question,
                BindingResult result,
                Authentication authentication,
                Model model) {


            if (result.hasErrors()) {
                String email = authentication.getName();

                model.addAttribute("courses",
                        courseRepository.findByCreatedBy(email));
                model.addAttribute("course", new Course());
                model.addAttribute("lesson", new Lesson());
                model.addAttribute("quizQuestion", question);

                return "instructor-dashboard";
            }

            quizQuestionRepository.save(question);

            return "redirect:/instructor/dashboard";
        }




    }




