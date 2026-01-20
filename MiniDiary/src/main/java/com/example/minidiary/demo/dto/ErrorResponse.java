package com.example.minidiary.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now(); // 언제 에러가 났는지
    private final int status;   // HTTP 상태 코드 (400, 404, 500 등)
    private final String error; // 에러 이름
    private final String message; // 사용자에게 보여줄 메시지
}