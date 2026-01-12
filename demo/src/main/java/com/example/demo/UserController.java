package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users") // 이 컨트롤러의 모든 API는 /users로 시작합니다.
public class UserController {

    private final UserService userService;

    // 생성자 주입 (DI): 스프링이 알아서 Service 객체를 넣어줍니다.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public String createUser(@RequestParam String name) {
        userService.addUser(name);
        return "등록 완료!";
    }

    @GetMapping("/search")
    public List<User> searchUser(@RequestParam("name") String name) {
        return userService.getUserByName(name);
    }
}
