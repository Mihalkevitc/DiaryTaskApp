package com.example.repositories;

import com.example.models.DiaryEntry;
import com.example.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {
    Page<DiaryEntry> findByUser(User user, Pageable pageable);

    List<DiaryEntry> findByUserAndTitleContainingOrContentContaining(User user, String title, String content);

    @Query("SELECT e FROM DiaryEntry e WHERE e.user = :user AND (e.title LIKE %:query% OR e.content LIKE %:query%) AND e.importance = :importance")
    List<DiaryEntry> findByUserAndQueryAndImportance(@Param("user") User user, @Param("query") String query, @Param("importance") Integer importance);
}
