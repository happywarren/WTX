package com.lt.feedback.exception;


public class FeedbackException extends RuntimeException {

    public FeedbackException() {
        super();
    }

    public FeedbackException(String message) {
        super(message);
    }

    public FeedbackException(Throwable cause) {
        super(cause);
    }

    public FeedbackException(String message, Throwable cause) {
        super(message, cause);
    }

}