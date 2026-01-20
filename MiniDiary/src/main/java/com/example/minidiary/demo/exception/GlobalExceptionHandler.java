package com.example.minidiary.demo.exception;


import com.example.minidiary.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 전역에서 발생하는 예외를 감지하는 어노테이션
public class GlobalExceptionHandler {

    // 1. 의도적으로 발생시킨 에러 (ResponseStatusException) 처리
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getStatusCode().value(),
                ex.getStatusCode().toString(),
                ex.getReason()
        );
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    // 2. @Valid 유효성 검사 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // 에러가 여러 개일 경우 첫 번째 메시지만 가져오거나, 맵으로 만듦
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // 가장 첫 번째 에러 메시지만 대표로 보여주는 방식 선택
        String firstErrorMessage = errors.values().stream().findFirst().orElse("유효성 검사 실패");

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                firstErrorMessage // 메시지가 여기 들어감
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 3. 예상치 못한 모든 에러 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요." // 보안상 상세 내용은 숨김
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}