package com.example.minidiary.demo.dto;

import com.example.minidiary.demo.Diary;
import jakarta.validation.constraints.NotBlank; // 유효성 검사
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

public class DiaryDTO {

    // 1. 요청용 DTO (데이터 받을 때)
    @Getter @Setter @NoArgsConstructor
    public static class Request {
        @NotBlank(message = "내용은 필수입니다!") // 빈 문자열이나 null이 오면 에러 처리
        private String content;
    }

    // 2. 응답용 DTO (데이터 줄 때)
    @Getter
    public static class Response {
        private final Long id;
        private final String content;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        // Entity -> DTO 변환 생성자
        public Response(Diary diary) {
            this.id = diary.getId();
            this.content = diary.getContent();
            this.createdAt = diary.getCreatedAt();
            this.updatedAt = diary.getUpdatedAt();
        }
    }
}