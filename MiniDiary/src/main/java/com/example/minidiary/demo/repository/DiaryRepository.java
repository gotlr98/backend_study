package com.example.minidiary.demo.repository;


import com.example.minidiary.demo.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    // 최신순 정렬을 위해 JPA의 메서드 명명 규칙 사용
    List<Diary> findAllByOrderByCreatedAtDesc();
}
