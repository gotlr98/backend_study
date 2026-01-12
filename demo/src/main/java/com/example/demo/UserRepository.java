package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository<대상엔티티, PK타입>을 상속받으면
// save(), findAll(), findById() 같은 메서드가 자동으로 생성됩니다.
public interface UserRepository extends JpaRepository<User, Long> {
    // 메서드 이름만 이렇게 지으면 JPA가 "SELECT * FROM users WHERE name = ?" 쿼리를 자동으로 만듭니다.
    List<User> findByName(String name);
}
