package com.example.minidiary.demo.controller;

import com.example.minidiary.demo.dto.UserDTO;
import com.example.minidiary.demo.model.User;
import com.example.minidiary.demo.repository.UserRepository;
import com.example.minidiary.demo.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Auth API", description = "로그인 및 회원가입")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public String signup(@RequestBody UserDTO.LoginRequest request) {
        userRepository.save(new User(request.getUsername(), request.getPassword()));
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO.LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .filter(u -> u.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증 실패"));

        // JWT 토큰 발급!
        return jwtUtil.createToken(user.getUsername());
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "로그아웃 성공";
    }
}