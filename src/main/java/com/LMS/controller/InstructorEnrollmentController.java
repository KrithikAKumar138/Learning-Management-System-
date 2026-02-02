package com.LMS.controller;

import com.LMS.entity.Enrollment;
import com.LMS.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/instructor")
public class InstructorEnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @GetMapping("/course/{courseId}/students")
    public String viewEnrolledStudents(@PathVariable Long courseId, Model model) {

        List<Enrollment> enrollments =
                enrollmentRepository.findByCourseId(courseId);

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("courseId", courseId);

        return "instructor-enrollments";
    }
}
