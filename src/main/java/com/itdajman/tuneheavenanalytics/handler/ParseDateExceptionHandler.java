package com.itdajman.tuneheavenanalytics.handler;

import com.itdajman.tuneheavenanalytics.exception.ReportException;
import com.itdajman.tuneheavenanalytics.model.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ParseDateExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageDto> illegalArgumentExceptionErrorResponse(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(illegalArgumentException.getMessage()));
    }

    @ExceptionHandler(ReportException.class)
    public ResponseEntity<ErrorMessageDto> reportExceptionErrorResponse(ReportException reportException) {
        return ResponseEntity.status(reportException.getHttpStatus()).body(new ErrorMessageDto(reportException.getMessage()));
    }

}
