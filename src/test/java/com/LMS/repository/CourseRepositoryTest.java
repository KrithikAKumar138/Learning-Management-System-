package com.LMS.repository;

import com.LMS.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findByApprovedTrue_shouldReturnApprovedCourses() {

        Course course = new Course();
        course.setTitle("JUnit Test Course");
        course.setApproved(true);

        courseRepository.save(course);

        List<Course> courses = courseRepository.findByApprovedTrue();

        assertFalse(courses.isEmpty());
    }
}


