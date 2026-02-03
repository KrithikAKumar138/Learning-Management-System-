package com.LMS.controller;

import com.LMS.entity.Course;
import com.LMS.entity.Enrollment;
import com.LMS.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    @WithMockUser(username = "student1", roles = "STUDENT")
    void shouldReturnStudentCoursesPage() throws Exception {

        Course course = new Course();
        course.setTitle("Java Basics");

        Enrollment enrollment = new Enrollment();
        enrollment.setStatus("In Progress");
        enrollment.setCourse(course);


        Mockito.when(studentService
                        .getFreshEnrollmentsForStudent(Mockito.any(), Mockito.eq("All")))
                .thenReturn(List.of(enrollment));

        Mockito.when(studentService.getAvailableCourses())
                .thenReturn(List.of(new Course()));

        mockMvc.perform(get("/student/courses")
                        .param("filter", "All"))
                .andExpect(status().isOk())
                .andExpect(view().name("student-courses"))
                .andExpect(model().attributeExists("enrollments"))
                .andExpect(model().attributeExists("availableCourses"))
                .andExpect(model().attributeExists("totalCourses"))
                .andExpect(model().attributeExists("inProgress"))
                .andExpect(model().attributeExists("completed"))
                .andExpect(model().attributeExists("notStarted"));
    }

    @Test
    @WithMockUser(username = "student1", roles = "STUDENT")
    void shouldEnrollStudentSuccessfully() throws Exception {

        mockMvc.perform(post("/student/enroll/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/courses"))
                .andExpect(flash().attribute("message", "Successfully enrolled!"))
                .andExpect(flash().attribute("messageType", "success"));
    }

    @Test
    @WithMockUser(username = "student1", roles = "STUDENT")
    void shouldFailEnrollment() throws Exception {

        Mockito.doThrow(new RuntimeException("Already enrolled"))
                .when(studentService)
                .enrollStudent(Mockito.eq(1L), Mockito.any());

        mockMvc.perform(post("/student/enroll/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/courses"))
                .andExpect(flash().attribute("message", "Already enrolled"))
                .andExpect(flash().attribute("messageType", "error"));
    }


    @Test
    @WithMockUser(username = "student1", roles = "STUDENT")
    void shouldUpdateProgress() throws Exception {

        mockMvc.perform(post("/student/progress/10")
                        .param("progress", "75")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Progress updated to 75.0%"));

        Mockito.verify(studentService)
                .updateProgress(10L, 75);
    }


    @Test
    @WithMockUser(username = "student1", roles = "STUDENT")
    void shouldMarkCourseAsCompleted() throws Exception {

        mockMvc.perform(post("/student/complete/10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Course marked as completed"));

        Mockito.verify(studentService)
                .updateProgress(10L, 100);
    }
}


