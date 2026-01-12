package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // 생성자 주입
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        // "SELECT * FROM user" 쿼리가 자동 실행됨
        return userRepository.findAll();
    }

    public void addUser(String name) {
        User user = new User(name);
        // "INSERT INTO user ..." 쿼리가 자동 실행됨
        userRepository.save(user);
    }

    public List<User> getUserByName(String name) {
        return userRepository.findByName(name);
    }
}