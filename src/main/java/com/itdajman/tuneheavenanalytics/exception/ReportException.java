package com.itdajman.tuneheavenanalytics.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReportException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ReportException(String message, Throwable e, HttpStatus httpStatus) {
        super(message, e);
        this.httpStatus = httpStatus;
    }
}
