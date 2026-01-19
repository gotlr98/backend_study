package com.example.minidiary.demo.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 매핑 정보만 주는 부모 클래스
@EntityListeners(AuditingEntityListener.class) // 스프링의 감시자가 이 클래스를 지켜봄
public abstract class BaseTimeEntity {

    @CreatedDate // 생성될 때 시간 자동 저장
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정될 때 시간 자동 저장
    private LocalDateTime updatedAt;
}
