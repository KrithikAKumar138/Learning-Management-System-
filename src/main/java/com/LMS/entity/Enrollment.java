package com.LMS.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDateTime enrolledDate;
    private double progress = 0.0;
    private String status = "Not Started";



    public Enrollment() {
        this.enrolledDate = LocalDateTime.now();
    }
    @ElementCollection
    @CollectionTable(
            name = "enrollment_completed_lessons",
            joinColumns = @JoinColumn(name = "enrollment_id")
    )
    @Column(name = "lesson_id")
    private List<Long> completedLessonIds = new ArrayList<>();


    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
        this.enrolledDate = LocalDateTime.now();
        this.progress = 0.0;
        this.status = "Not Started";
    }


    public void setProgress(double progress) {
        this.progress = progress;
        if (progress >= 100) {
            this.status = "Completed";
        } else if (progress > 0) {
            this.status = "In Progress";
        } else {
            this.status = "Not Started";
        }
    }
}
