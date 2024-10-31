package com.jay.paper_summarizer.exceptions;
public class APIException extends RuntimeException {


    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }
}
