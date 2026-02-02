package com.LMS.repository;

import com.LMS.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCreatedBy(String createdBy);

    List<Course> findByApprovedTrue();
}
