package com.user.exception;

import com.user.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(UserRequiredFields.class)
    public ResponseEntity<ErrorResponse> handleUserRequiredFields(UserRequiredFields exception){
        return new ResponseEntity<>(new ErrorResponse(404, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserIdNotFound(UserIdNotFoundException exception){
        return new ResponseEntity<>(new ErrorResponse(404, exception.getMessage()), HttpStatus.NOT_FOUND);
    }

}
