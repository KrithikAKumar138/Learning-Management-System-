package com.LMS.controller;

import com.LMS.entity.Course;
import com.LMS.entity.Lesson;
import com.LMS.entity.QuizQuestion;
import com.LMS.exception.ResourceNotFoundException;
import com.LMS.repository.CourseRepository;
import com.LMS.repository.QuizQuestionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InstructorPageController.class)
@WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
class InstructorPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseRepository courseRepository;

    @MockitoBean
    private QuizQuestionRepository quizQuestionRepository;

    // ----------------------------------------
    // DASHBOARD
    // ----------------------------------------
    @Test
    void shouldLoadInstructorDashboard() throws Exception {

        when(courseRepository.findByCreatedBy("instructor@test.com"))
                .thenReturn(List.of(new Course()));

        mockMvc.perform(get("/instructor/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("instructor-dashboard"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("lesson"))
                .andExpect(model().attributeExists("quizQuestion"));
    }

    // ----------------------------------------
    // COURSE PLAYER - SUCCESS
    // ----------------------------------------
    @Test
    void shouldLoadInstructorCoursePlayer() throws Exception {

        Course course = new Course();
        course.setId(1L);
        course.setCreatedBy("instructor@test.com");

        Lesson lesson = new Lesson();
        lesson.setId(10L);
        lesson.setOrderNumber(1);

        course.setLessons(List.of(lesson));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(quizQuestionRepository.findByCourseId(1L))
                .thenReturn(List.of(new QuizQuestion()));

        mockMvc.perform(get("/instructor/course/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("instructor-course-player"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("lessons"))
                .andExpect(model().attributeExists("currentLesson"))
                .andExpect(model().attributeExists("quizQuestions"))
                .andExpect(model().attribute("basePath", "/instructor"));
    }

    // ----------------------------------------
    // COURSE PLAYER - NOT OWNER
    // ----------------------------------------
    @Test
    void shouldRedirectIfInstructorIsNotOwner() throws Exception {

        Course course = new Course();
        course.setId(1L);
        course.setCreatedBy("other@test.com");

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        mockMvc.perform(get("/instructor/course/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/instructor/dashboard"));
    }

    // ----------------------------------------
    // COURSE PLAYER - NOT FOUND
    // ----------------------------------------
    @Test
    void shouldThrowResourceNotFoundIfCourseMissing() throws Exception {

        when(courseRepository.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/instructor/course/99"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}
