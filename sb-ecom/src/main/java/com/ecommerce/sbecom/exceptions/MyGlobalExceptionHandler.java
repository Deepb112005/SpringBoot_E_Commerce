package com.ecommerce.sbecom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errorResponse = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(e ->{

            String fieldName = ((FieldError)e).getField();
            String message = ((FieldError)e).getDefaultMessage();
            errorResponse.put(fieldName,message);
        });

        return new ResponseEntity<>(errorResponse , HttpStatus.BAD_REQUEST);
    }


}
