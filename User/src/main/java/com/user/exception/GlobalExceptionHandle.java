package com.user.exception;

import com.user.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(UserRequiredFields.class)
    public ResponseEntity<ErrorResponse> handleUserRequiredFields(UserRequiredFields exception){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserIdNotFound(UserIdNotFoundException exception){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception){
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(error -> error.getField()+ ": " + error.getDefaultMessage())
                .toList();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PageNumberException.class)
    public ResponseEntity<ErrorResponse> handlePageNumberException(PageNumberException exception){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException exception){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
