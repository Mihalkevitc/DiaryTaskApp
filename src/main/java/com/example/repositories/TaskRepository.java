package com.example.repositories;

import com.example.models.Task;
import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByUserAndTaskDateOrderByStartTimeAsc(User user, LocalDate taskDate);
    List<Task> findAllByUserAndTaskDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
