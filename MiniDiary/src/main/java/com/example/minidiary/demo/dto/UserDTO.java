package com.example.minidiary.demo.dto;

import lombok.Getter;
import lombok.Setter;

public class UserDTO {
    @Getter
    @Setter
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
