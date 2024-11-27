package com.example.controllers;

import com.example.models.DiaryEntry;
import com.example.models.User;
import com.example.security.CustomUserDetails;
import com.example.services.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @GetMapping("/diary")
    public String viewDiary(Model model, @AuthenticationPrincipal CustomUserDetails userDetails,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        User user = userDetails.getUser();
        Page<DiaryEntry> entriesPage = diaryService.getAllEntries(user, page, size);

        List<DiaryEntry> truncatedEntries = entriesPage.getContent().stream()
                .peek(entry -> {
                    if (entry.getContent() != null && entry.getContent().length() > 100) {
                        entry.setContent(entry.getContent().substring(0, 100) + "...");
                    }
                })
                .toList();

        model.addAttribute("entries", truncatedEntries);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", entriesPage.getTotalPages());
        model.addAttribute("totalEntries", entriesPage.getTotalElements());
        model.addAttribute("pageSize", size);

        return "diary";
    }

    @GetMapping("/diary/new")
    public String createNewEntry(Model model) {
        model.addAttribute("diaryEntry", new DiaryEntry());
        return "create-entry";  // Страница создания новой записи
    }

    @PostMapping("/diary")
    public String addNewEntry(@ModelAttribute("diaryEntry") DiaryEntry entry,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam("importance") int importance) {
        entry.setUser(userDetails.getUser());
        entry.setDate(LocalDateTime.now());

        if(importance==0)
        {
            importance = 1;
        }

        entry.setImportance(importance);
        diaryService.addEntry(entry);
        return "redirect:/diary";
    }

    @GetMapping("/diary/edit/{id}")
    public String editDiaryEntry(@PathVariable("id") Long id, Model model) {
        // Находим запись по ID
        DiaryEntry entry = diaryService.getEntryById(id).orElseThrow(() -> new IllegalArgumentException("Invalid diary entry id: " + id));
        model.addAttribute("diaryEntry", entry);  // Добавляем запись в модель для отображения в форме редактирования
        return "edit-entry";  // Страница для редактирования записи
    }

    @PostMapping("/diary/edit/{id}")
    public String saveEditedEntry(@PathVariable("id") Long id,
                                  @ModelAttribute("diaryEntry") DiaryEntry entry,
                                  @RequestParam("importance") int importance,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        Optional<DiaryEntry> existingEntry = diaryService.getEntryById(id);
        if (existingEntry.isPresent()) {
            DiaryEntry existing = existingEntry.get();

            entry.setId(id);
            if (entry.getUser() == null) {
                entry.setUser(userDetails.getUser());
            }

            entry.setDate(LocalDateTime.now());

            entry.setImportance(importance);

            diaryService.addEntry(entry);
            return "redirect:/diary";
        }
        return "redirect:/diary";
    }

    @GetMapping("/diary/delete/{id}")
    public String deleteEntry(@PathVariable("id") Long id)
    {
        diaryService.deleteEntry(id);
        return "redirect:/diary";
    }


    @PostMapping("/diary/updateImportance/{id}")
    @ResponseBody
    public ResponseEntity<Void> updateImportance(@PathVariable("id") Long id, @RequestParam("importance") int importance) {
        //System.out.println("Received updateImportance for ID: " + id + " with importance: " + importance);

        Optional<DiaryEntry> entryOptional = diaryService.getEntryById(id);
        if (entryOptional.isPresent()) {
            DiaryEntry entry = entryOptional.get();
            entry.setImportance(importance);
            diaryService.addEntry(entry); // Сохраняем обновлённую запись
            return ResponseEntity.ok().build(); // Возвращаем успешный ответ
        }

        //System.out.println("Entry with ID " + id + " not found");
        return ResponseEntity.notFound().build(); // Если запись не найдена, возвращаем 404
    }

    @GetMapping("/diary/search")
    public String searchEntries(@RequestParam("query") Optional<String> query,
                                @RequestParam(value = "importance", required = false) Integer importance,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                Model model) {
        // Логируем значение importance
        System.out.println("Importance: " + importance);

        User user = userDetails.getUser();

        List<DiaryEntry> results;
        if ((query.isPresent() && !query.get().isBlank()) || importance != null) {
            results = diaryService.searchEntries(user, query.orElse(""), importance);
        } else {
            results = List.of();  // Пустой список, если запрос отсутствует и нет важности
        }


        // Обрезка контента каждой записи до 100 символов с многоточием
        List<DiaryEntry> truncatedResults = results.stream()
                .peek(entry -> {
                    if (entry.getContent() != null && entry.getContent().length() > 100) {
                        entry.setContent(entry.getContent().substring(0, 100) + "...");
                    }
                })
                .toList();

        model.addAttribute("entries", truncatedResults);
        model.addAttribute("query", query.orElse(""));
        model.addAttribute("importance", importance);  // Добавляем важность в модель, чтобы она оставалась при перезагрузке
        return "search-results";  // Страница с результатами поиска
    }

}
