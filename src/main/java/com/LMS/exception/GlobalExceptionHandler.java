package com.LMS.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public void ignoreStaticResources() {
        // intentionally empty → ignore favicon.ico & similar
    }

    // ---------------------------
    // RESOURCE NOT FOUND (404)
    // ---------------------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    // ---------------------------
    // FORM VALIDATION ERRORS
    // ---------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidation(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("errorMessage", "Please fill all required fields");
        return "instructor-dashboard";
    }

    // ---------------------------
    // FILE SIZE ERROR
    // ---------------------------
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileSize(MaxUploadSizeExceededException ex, Model model) {
        model.addAttribute("errorMessage", "Uploaded file is too large");
        return "instructor-dashboard";
    }

    // ---------------------------
    // BUSINESS LOGIC ERRORS (LESSON / QUIZ)
    // ---------------------------
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "instructor-dashboard";
    }

    // ---------------------------
    // FALLBACK (500)
    // ---------------------------
    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        ex.printStackTrace(); // 🔥 THIS LINE IS CRITICAL
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(LessonUploadException.class)
    public String handleLessonUpload(LessonUploadException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "instructor-dashboard";
    }


}
