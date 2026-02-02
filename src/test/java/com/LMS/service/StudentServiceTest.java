package com.LMS.service;

import com.LMS.entity.Course;
import com.LMS.entity.Enrollment;
import com.LMS.entity.User;
import com.LMS.repository.CourseRepository;
import com.LMS.repository.EnrollmentRepository;
import com.LMS.repository.LessonRepository;
import com.LMS.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private Authentication authentication;

    @Test
    void enrollStudent_shouldThrowException_whenAlreadyEnrolled() {

        // 1️⃣ Fake authentication
        when(authentication.getName()).thenReturn("student@lms.com");

        // 2️⃣ Fake student
        User student = new User();
        student.setId(1L);
        student.setEmail("student@lms.com");

        when(userRepository.findByEmail("student@lms.com"))
                .thenReturn(Optional.of(student));

        // 3️⃣ Fake course
        Course course = new Course();
        course.setId(10L);

        when(courseRepository.findById(10L))
                .thenReturn(Optional.of(course));

        // 4️⃣ Say student is already enrolled
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 10L))
                .thenReturn(true);

        // 5️⃣ Expect exception
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> studentService.enrollStudent(10L, authentication)
        );

        assertEquals("Already enrolled in this course", exception.getMessage());
    }
    @Test
    void enrollStudent_shouldCreateEnrollment_whenNotEnrolled() {

        when(authentication.getName()).thenReturn("student@lms.com");

        User student = new User();
        student.setId(1L);
        student.setEmail("student@lms.com");

        Course course = new Course();
        course.setId(10L);

        when(userRepository.findByEmail("student@lms.com"))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(10L))
                .thenReturn(Optional.of(course));

        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 10L))
                .thenReturn(false);

        Enrollment savedEnrollment = new Enrollment(student, course);

        when(enrollmentRepository.save(any(Enrollment.class)))
                .thenReturn(savedEnrollment);

        Enrollment result =
                studentService.enrollStudent(10L, authentication);

        assertNotNull(result);
        assertEquals(student, result.getStudent());
        assertEquals(course, result.getCourse());
    }
    @Test
    void updateProgress_shouldThrowException_whenProgressIsInvalid() {

        Enrollment enrollment = new Enrollment();
        enrollment.setId(5L);

        when(enrollmentRepository.findById(5L))
                .thenReturn(Optional.of(enrollment));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> studentService.updateProgress(5L, 150)
        );

        assertEquals("Progress must be between 0 and 100", exception.getMessage());
    }



}
