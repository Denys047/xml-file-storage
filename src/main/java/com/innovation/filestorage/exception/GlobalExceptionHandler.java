package com.innovation.filestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFileNameException.class)
    public ResponseEntity<ProblemDetail> handleInvalidFileNameException(InvalidFileNameException ex) {
        return ResponseEntity.of(buildProblemDetail(ex.getMessage())).build();
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleFileAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return ResponseEntity.of(buildProblemDetail(ex.getMessage())).build();
    }

    @ExceptionHandler(XmlParsingException.class)
    public ResponseEntity<ProblemDetail> handleXmlParsingException(XmlParsingException ex) {
        return ResponseEntity.of(buildProblemDetail(ex.getMessage())).build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleFileNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.of(buildProblemDetail(ex.getMessage(), HttpStatus.NOT_FOUND)).build();
    }

    private ProblemDetail buildProblemDetail(String message) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
    }

    private ProblemDetail buildProblemDetail(String message, HttpStatus status) {
        return ProblemDetail.forStatusAndDetail(status, message);
    }

}
