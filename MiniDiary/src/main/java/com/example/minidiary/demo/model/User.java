package com.example.minidiary.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; // 아이디

    private String password; // 비밀번호

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
