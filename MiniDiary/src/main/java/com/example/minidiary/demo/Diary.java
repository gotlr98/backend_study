package com.example.minidiary.demo;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diaries")
@Getter @Setter // 수정 기능을 위해 Setter도 추가
@NoArgsConstructor
@AllArgsConstructor
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createdAt; // 전공자라면 생성시간 정도는 있어야겠죠?

    public Diary(String content) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
