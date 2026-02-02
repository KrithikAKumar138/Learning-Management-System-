package com.LMS.controller;

import com.LMS.entity.Enrollment;
import com.LMS.entity.QuizQuestion;
import com.LMS.repository.QuizQuestionRepository;
import com.LMS.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student/quiz")
public class StudentQuizController {

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private StudentService studentService;


    @GetMapping("/{courseId}")
    public String startQuiz(@PathVariable Long courseId, Model model) {

        List<QuizQuestion> questions =
                quizQuestionRepository.findByCourseId(courseId);

        model.addAttribute("questions", questions);
        model.addAttribute("courseId", courseId);

        return "student-quiz";
    }


    @PostMapping("/submit")
    public String submitQuiz(@RequestParam Long courseId,
                             @RequestParam Map<String, String> answers,
                             Model model,
                             Authentication authentication) {

        List<QuizQuestion> questions =
                quizQuestionRepository.findByCourseId(courseId);

        int correct = 0;

        for (QuizQuestion q : questions) {
            String userAnswer = answers.get("q_" + q.getId());
            if (q.getCorrectOption().equals(userAnswer)) {
                correct++;
            }
        }

        int total = questions.size();
        int score = (correct * 100) / total;
        boolean passed = score >= 60;


        if (passed) {
            Enrollment enrollment =
                    studentService.getEnrollmentByStudentEmailAndCourseId(
                            authentication.getName(), courseId);

            enrollment.setProgress(100); // 90% lessons + 10% quiz
            enrollment.setStatus("Completed");

            studentService.saveEnrollment(enrollment);
        }
        model.addAttribute("score", score);
        model.addAttribute("passed", passed);

        return "student-quiz"; // SAME PAGE
    }

}
