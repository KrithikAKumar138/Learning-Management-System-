package com.LMS.exception;

public class LessonUploadException extends IllegalStateException {

    public LessonUploadException(String message) {
        super(message);
    }

    public LessonUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
