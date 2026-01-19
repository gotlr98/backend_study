package com.example.minidiary.demo.controller;

import com.example.minidiary.demo.model.Diary;
import com.example.minidiary.demo.repository.DiaryRepository;
import com.example.minidiary.demo.dto.DiaryDTO;
import com.example.minidiary.demo.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Diary API", description = "나만의 메모장 API") // Swagger 그룹 이름
@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryRepository diaryRepository;
    private final JwtUtil jwtUtil;

    @PostMapping
    public DiaryDTO.Response create(
            @RequestHeader("Authorization") String bearerToken,
            @Valid @RequestBody DiaryDTO.Request request) {

        try {
            // "Bearer " 뒷부분의 실제 토큰만 추출
            String token = bearerToken.substring(7);
            String username = jwtUtil.getUsername(token);

            Diary diary = new Diary(request.getContent() + " (By: " + username + ")");
            return new DiaryDTO.Response(diaryRepository.save(diary));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }

    @Operation(summary = "전체 조회 (페이징)", description = "메모를 페이징하여 조회합니다. (기본 10개씩, 최신순)")
    @GetMapping
    public Page<DiaryDTO.Response> getAll(
            // @PageableDefault: 클라이언트가 아무 조건 없이 요청했을 때의 기본 설정
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // 1. findAll(pageable)이 DB에서 딱 필요한 만큼만 가져옴
        // 2. map(): Page 안에 있는 Diary 엔티티들을 하나씩 DTO로 변환
        return diaryRepository.findAll(pageable)
                .map(DiaryDTO.Response::new);
    }

    @Operation(summary = "메모 수정", description = "특정 ID의 메모 내용을 수정합니다.")
    @PutMapping("/{id}")
    public DiaryDTO.Response update(@PathVariable Long id, @Valid @RequestBody DiaryDTO.Request request) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메모 없음"));

        diary.setContent(request.getContent());
        Diary updatedDiary = diaryRepository.save(diary);
        return new DiaryDTO.Response(updatedDiary);
    }

    @Operation(summary = "메모 삭제", description = "특정 ID의 메모 내용을 삭제합니다.")
    @DeleteMapping("/{id}")
    public DiaryDTO.Response delete(@PathVariable Long id){
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제할 메모가 없습니다."));
        diaryRepository.delete(diary);
        return new DiaryDTO.Response(diary);
    }
}