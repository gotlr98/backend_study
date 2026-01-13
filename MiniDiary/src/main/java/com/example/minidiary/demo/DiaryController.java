package com.example.minidiary.demo;

import com.example.minidiary.demo.dto.DiaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "메모 작성", description = "새로운 메모를 등록합니다.") // API 설명
    @PostMapping
    // @Valid: DTO의 @NotBlank 조건을 검사함
    // @RequestBody: 이제 URL 파라미터가 아니라 JSON Body로 데이터를 받음
    public DiaryDTO.Response create(@Valid @RequestBody DiaryDTO.Request request) {
        Diary diary = new Diary(request.getContent());
        Diary savedDiary = diaryRepository.save(diary);
        return new DiaryDTO.Response(savedDiary); // Entity 대신 DTO 리턴
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