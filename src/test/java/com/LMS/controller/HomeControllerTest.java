package com.LMS.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.LMS.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔥 IMPORTANT
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldLoadLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(java.util.Optional.of(new com.LMS.entity.User()));

        when(passwordEncoder.encode(anyString()))
                .thenReturn("encodedPassword");

        mockMvc.perform(post("/register")
                        .param("name", "Test User")
                        .param("email", "test@test.com")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("emailError"));
    }
    @Test
    void shouldFailValidation_whenNameIsMissing() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "test@test.com")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors());
    }


}
