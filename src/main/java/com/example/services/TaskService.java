package com.example.services;

import com.example.models.Task;
import com.example.models.User;
import com.example.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addTask(Task task) {
        taskRepository.save(task);
    }

    // Метод для получения задач на определенную дату с сортировкой по времени начала
    public List<Task> getTasksForDate(User user, LocalDate date) {
        return taskRepository.findByUserAndTaskDateOrderByStartTimeAsc(user, date);  // Сортировка по времени начала (по возрастанию)
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Задача с id " + id + " не найдена"));
    }


    public List<Task> getAllTasks(User user) {
        return taskRepository.findByUser(user);
    }

    public void updateTask(Task task) {
        taskRepository.save(task);  // Обновление задачи в базе данных
    }

    // Сохранение задачи в базе данных
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksForDateRange(User user, LocalDate start, LocalDate end) {
        return taskRepository.findAllByUserAndTaskDateBetween(user, start, end);
    }



}
