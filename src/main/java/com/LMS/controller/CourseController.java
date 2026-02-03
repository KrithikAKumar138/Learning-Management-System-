package com.LMS.controller;
import com.LMS.entity.Course;
import com.LMS.entity.Lesson;
import com.LMS.entity.QuizQuestion;
import com.LMS.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/create")
    public String createCourse(
            @Valid @ModelAttribute("course") Course course,
            BindingResult result,
            Authentication authentication,
            Model model) {

        if (result.hasErrors()) {
            String email = authentication.getName();

            model.addAttribute("courses",
                    courseRepository.findByCreatedBy(email));

            model.addAttribute("course", course);
            model.addAttribute("lesson", new Lesson());
            model.addAttribute("quizQuestion", new QuizQuestion());

            return "instructor-dashboard";
        }


        course.setCreatedBy(authentication.getName());
        course.setApproved(false);

        courseRepository.save(course);

        return "redirect:/instructor/dashboard";
    }




    @GetMapping("/approved")
    @ResponseBody
    public List<Course> getApprovedCourses() {
        return courseRepository.findByApprovedTrue();
    }

    @PostMapping("/{id}/delete")
    @ResponseBody
    public String deleteCourse(@PathVariable Long id) {
        courseRepository.deleteById(id);
        return "Course deleted successfully";
    }

}