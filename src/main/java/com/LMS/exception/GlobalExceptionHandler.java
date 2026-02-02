package com.LMS.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

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
        model.addAttribute("errorMessage", "Something went wrong. Please try again.");
        return "error";
    }
}
