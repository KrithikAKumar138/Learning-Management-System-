package com.LMS.controller;

import com.LMS.repository.CourseRepository;
import com.LMS.repository.UserRepository;
import com.LMS.repository.QuizQuestionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminPageController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseRepository courseRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private QuizQuestionRepository quizQuestionRepository;

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void adminDashboardLoads() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-dashboard"));
    }
}
