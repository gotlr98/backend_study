package com.example.minidiary.demo;

import com.example.minidiary.demo.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diaries")
@Getter @Setter // 수정 기능을 위해 Setter도 추가
@NoArgsConstructor
@AllArgsConstructor
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    public Diary(String content) {
        this.content = content;
        // ❌ this.createdAt = LocalDateTime.now(); <-- (JPA가 알아서 넣어줌)
    }
}
