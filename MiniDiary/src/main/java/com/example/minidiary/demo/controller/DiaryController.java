package com.example.minidiary.demo.controller;

import com.example.minidiary.demo.Diary;
import com.example.minidiary.demo.repository.DiaryRepository;
import com.example.minidiary.demo.dto.DiaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Diary API", description = "나만의 메모장 API") // Swagger 그룹 이름
@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryRepository diaryRepository;

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

    @Operation(summary = "전체 조회", description = "모든 메모를 최신순으로 가져옵니다.")
    @GetMapping
    public List<DiaryDTO.Response> getAll() {
        return diaryRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(DiaryDTO.Response::new) // 리스트 내부의 Entity들을 DTO로 변환
                .collect(Collectors.toList());
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