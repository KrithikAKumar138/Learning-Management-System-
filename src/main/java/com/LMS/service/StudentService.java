package com.LMS.service;

import com.LMS.entity.Course;
import com.LMS.entity.Enrollment;
import com.LMS.entity.Lesson;
import com.LMS.entity.User;
import com.LMS.exception.ResourceNotFoundException;
import com.LMS.repository.CourseRepository;
import com.LMS.repository.EnrollmentRepository;
import com.LMS.repository.LessonRepository;
import com.LMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private static final double LESSON_PROGRESS = 90.0;


    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    public Enrollment enrollStudent(Long courseId, Authentication authentication) {
        String studentEmail = authentication.getName();

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalStateException("You are already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment(student, course);
        enrollment.setStatus("Not Started");
        enrollment.setProgress(0);

        return enrollmentRepository.save(enrollment);
    }

    public List<Course> getAvailableCourses() {
        return courseRepository.findByApprovedTrue();
    }

    public List<Enrollment> getStudentCourses(Authentication authentication) {
        User student = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return enrollmentRepository.findByStudentId(student.getId());
    }

    public List<Enrollment> getCoursesByStatus(Authentication authentication, String status) {
        User student = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if ("All".equalsIgnoreCase(status)) {
            return enrollmentRepository.findByStudentId(student.getId());
        }

        return enrollmentRepository.findByStudentIdAndStatus(student.getId(), status);
    }

    public void updateProgress(Long enrollmentId, double progress) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        if (progress < 0 || progress > 100) {
            throw new IllegalStateException("Progress must be between 0 and 100");
        }

        enrollment.setProgress(progress);
        enrollmentRepository.save(enrollment);
    }

    public Enrollment getEnrollmentByStudentEmailAndCourseId(String email, Long courseId) {
        User student = userRepository.findByEmail(email)
                .orElse(null);

        if (student == null) {
            return null;
        }

        return enrollmentRepository
                .findByStudentIdAndCourseId(student.getId(), courseId)
                .orElse(null);
    }

    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseIdOrderByOrderNumberAsc(courseId);
    }


    public Lesson getLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
    }
    public void saveEnrollment(Enrollment enrollment) {
        enrollmentRepository.save(enrollment);
    }

    public void markLessonCompleted(Long enrollmentId, Long lessonId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        if (!enrollment.getCompletedLessonIds().contains(lessonId)) {
            enrollment.getCompletedLessonIds().add(lessonId);
        }

        int totalLessons = lessonRepository
                .findByCourseIdOrderByOrderNumberAsc(
                        enrollment.getCourse().getId()
                ).size();

        int completedLessons = enrollment.getCompletedLessonIds().size();

        double lessonProgress =
                ((double) completedLessons / totalLessons) * LESSON_PROGRESS;

        enrollment.setProgress(Math.min(lessonProgress, 90.0));
        enrollment.setStatus("In Progress");

        enrollmentRepository.saveAndFlush(enrollment);
    }


    @Transactional(readOnly = true)
    public List<Enrollment> getFreshEnrollmentsForStudent(Authentication authentication, String status) {

        User student = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));


        if ("All".equalsIgnoreCase(status)) {
            return enrollmentRepository.findByStudentId(student.getId());
        }

        return enrollmentRepository.findByStudentIdAndStatus(student.getId(), status);
    }
}
