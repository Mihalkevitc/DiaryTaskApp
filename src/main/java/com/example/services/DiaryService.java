package com.example.services;

import com.example.models.DiaryEntry;
import com.example.models.User;
import com.example.repositories.DiaryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {
    private final DiaryEntryRepository diaryEntryRepository;

    @Autowired
    public DiaryService(DiaryEntryRepository diaryEntryRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
    }


    public void addEntry(DiaryEntry entry) {
        diaryEntryRepository.save(entry);
    }

    // Метод для получения всех записей с пагинацией
    public Page<DiaryEntry> getAllEntries(User user, int page, int size) {
        // Сортировка по дате в убывающем порядке
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("date")));
        return diaryEntryRepository.findByUser(user, pageable);
    }


    // Метод для получения записи по ID
    public Optional<DiaryEntry> getEntryById(Long id) {
        return diaryEntryRepository.findById(id);  // Находим запись по ID
    }

    public void deleteEntry(Long id) {
        diaryEntryRepository.deleteById(id);
    }

    public List<DiaryEntry> searchEntries(User user, String query, Integer importance) {
        if (importance != null) {
            //Если есть фильтр по важности
            return diaryEntryRepository.findByUserAndQueryAndImportance(user, query, importance);
        } else {
            //Если без важности
            return diaryEntryRepository.findByUserAndTitleContainingOrContentContaining(user, query, query);
        }
    }




}
