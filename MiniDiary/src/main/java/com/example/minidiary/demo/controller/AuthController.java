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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public String signup(@RequestBody UserDTO.LoginRequest request) {
        // 2. 비밀번호 암호화: "1234" -> "$2a$10$Dk/..." (알아볼 수 없는 문자로 변환)
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        userRepository.save(new User(request.getUsername(), encodedPassword));
        return "회원가입 성공";
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public String login(@RequestBody UserDTO.LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ID가 없습니다."));

        // 3. 비밀번호 검증: matches(입력받은비번, DB의암호화된비번)
        // 암호화된 건 복호화가 불가능하므로, 입력값을 똑같이 암호화해서 비교하는 방식입니다.
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다.");
        }

        return jwtUtil.createToken(user.getUsername());
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "로그아웃 성공";
    }
}