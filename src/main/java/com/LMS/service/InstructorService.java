package com.LMS.service;

import com.LMS.entity.Lesson;
import com.LMS.entity.Course;
import com.LMS.exception.LessonUploadException;
import com.LMS.exception.ResourceNotFoundException;
import com.LMS.repository.LessonRepository;
import com.LMS.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class InstructorService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    public void addLesson(Long courseId, String title, String content, MultipartFile file) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->  new ResourceNotFoundException("Course not found"));
        if (file == null || file.isEmpty()) {
            throw new LessonUploadException("Please select a lesson file");
        }


        try {
            String firebaseUrl =
                    firebaseStorageService.uploadFile(file, "lessons/" + courseId);

            Lesson lesson = new Lesson();
            lesson.setCourse(course);
            lesson.setCourseTitle(course.getTitle());
            lesson.setTitle(title);
            lesson.setContent(content);

            lesson.setFileName(file.getOriginalFilename());
            lesson.setFileType(file.getContentType());
            lesson.setFilePath(firebaseUrl);

            Long count = lessonRepository.countByCourseId(courseId);
            lesson.setOrderNumber(count.intValue() + 1);

            String mime = file.getContentType();

            if (mime != null && mime.startsWith("video")) {
                lesson.setContentType("VIDEO");
            } else if ("application/pdf".equals(mime)) {
                lesson.setContentType("PDF");
            } else if (mime != null && mime.startsWith("text")) {
                lesson.setContentType("TEXT");
                lesson.setContent(new String(file.getBytes()));
            } else {
                lesson.setContentType("FILE");
            }

            lessonRepository.save(lesson);

        } catch (Exception e) {
            throw new LessonUploadException("Lesson upload failed. Please try again.", e);
        }

    }
}



