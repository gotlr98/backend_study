package com.example.minidiary.demo;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor // 롬복: 생성자 주입 자동화
public class DiaryController {

    private final DiaryRepository diaryRepository;

    // 1. 메모 작성 (Create)
    @PostMapping
    public Diary create(@RequestParam String content) {
        return diaryRepository.save(new Diary(content));
    }

    // 2. 전체 조회 (Read)
    @GetMapping
    public List<Diary> getAll() {
        return diaryRepository.findAllByOrderByCreatedAtDesc();
    }

    // 3. 메모 수정 (Update)
    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestParam String newContent) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 메모가 없습니다."));
        diary.setContent(newContent);
        diaryRepository.save(diary);
        return "수정 성공!";
    }

    // 4. 메모 삭제 (Delete)
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        diaryRepository.deleteById(id);
        return "삭제 성공!";
    }
}
