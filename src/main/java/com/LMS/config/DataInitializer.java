package com.LMS.config;

import com.LMS.entity.Course;
import com.LMS.entity.Role;
import com.LMS.entity.User;
import com.LMS.repository.CourseRepository;
import com.LMS.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            CourseRepository courseRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            System.out.println("=== Initializing LMS Data ===");

            if (userRepository.findByEmail("admin@gamil.com").isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@gamil.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("✓ Created ADMIN user: admin@gamil.com / admin123");
            } else {
                System.out.println(" ADMIN user already exists");
            }


            if (userRepository.findByEmail("instructor@gamil.com").isEmpty()) {
                User instructor = new User();
                instructor.setName("Instructor");
                instructor.setEmail("instructor@gamil.com");
                instructor.setPassword(passwordEncoder.encode("instructor123"));
                instructor.setRole(Role.INSTRUCTOR);
                userRepository.save(instructor);
                System.out.println("✓ Created INSTRUCTOR user: instructor@gamil.com / instructor123");
            } else {
                System.out.println("INSTRUCTOR user already exists");
            }


            if (userRepository.findByEmail("student@gamil.com").isEmpty()) {
                User student = new User();
                student.setName("Student");
                student.setEmail("student@gamil.com");
                student.setPassword(passwordEncoder.encode("student123"));
                student.setRole(Role.STUDENT);
                userRepository.save(student);
                System.out.println("✓ Created STUDENT user: student@gamil.com / student123");
            } else {
                System.out.println(" STUDENT user already exists");
            }

            System.out.println("=== Data Initialization Complete ===");
        };
    }
}