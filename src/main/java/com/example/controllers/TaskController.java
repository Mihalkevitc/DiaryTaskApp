package com.example.controllers;

import com.example.models.DiaryEntry;
import com.example.models.Task;
import com.example.models.User;
import com.example.security.CustomUserDetails;
import com.example.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Метод для отображения задач на сегодня
    @GetMapping("/today")
    public String viewTasksForToday(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        LocalDate today = LocalDate.now();
        List<Task> tasks = taskService.getTasksForDate(user, today);
        // В контроллере можно вычислить текущий месяц
        LocalDate currentDate = LocalDate.now();
        String yearMonth = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        model.addAttribute("tasks", tasks);
        model.addAttribute("currentDate", today);
        model.addAttribute("yearMonth", yearMonth);

        return "tasks";  // Шаблон для отображения задач
    }



    // Метод для отображения задач по конкретной дате
    @GetMapping("/{date}")
    public String viewTasksForSpecificDate(@PathVariable("date") String date,
                                           Model model,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        LocalDate taskDate = LocalDate.parse(date);

        List<Task> tasks = taskService.getTasksForDate(user, taskDate);

        // В контроллере можно вычислить текущий месяц
        LocalDate currentDate = LocalDate.now();
        String yearMonth = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        model.addAttribute("tasks", tasks);
        model.addAttribute("currentDate", taskDate);
        model.addAttribute("yearMonth", yearMonth);
        return "tasks";
    }

    // Страница создания новой задачи
    @GetMapping("/new")
    public String createNewTask(Model model) {
        model.addAttribute("task", new Task());
        return "create-task";  // Страница создания новой задачи
    }

    // Добавление новой задачи
    @PostMapping
    public String addTask(@ModelAttribute("task") Task task, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        task.setUser(userDetails.getUser());

        // Установка текущей даты или другой логики
        if (task.getTaskDate() == null) {
            task.setTaskDate(LocalDate.now());
        }

        // Проверка, чтобы время завершения не было раньше времени начала
        if (task.getStartTime().compareTo(task.getEndTime()) > 0) {
            model.addAttribute("error", "Время завершения не может быть раньше времени начала.");
            return "create-task"; // Возвращаемся на страницу создания задачи с ошибкой
        }

        taskService.addTask(task);
        return "redirect:/tasks/today";
    }

    // Отображение формы редактирования
    @GetMapping("/edit/{id}")
    public String editTaskForm(@PathVariable("id") Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "edit-task";  // Шаблон формы редактирования
    }

    // Обработка изменений задачи
    @PostMapping("/edit/{id}")
    public String editTask(@PathVariable("id") Long id,
                           @ModelAttribute("task") Task updatedTask,
                           @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Task task = taskService.getTaskById(id);

        // Проверка, чтобы время завершения не было раньше времени начала
        if (updatedTask.getStartTime().compareTo(updatedTask.getEndTime()) > 0) {
            model.addAttribute("error", "Время завершения не может быть раньше времени начала.");
            model.addAttribute("task", updatedTask);
            return "edit-task"; // Возвращаемся на страницу редактирования с ошибкой
        }

        // Обновляем поля задачи
        task.setTitle(updatedTask.getTitle());
        task.setContent(updatedTask.getContent());
        task.setStartTime(updatedTask.getStartTime());
        task.setEndTime(updatedTask.getEndTime());
        task.setUser(userDetails.getUser());  // Убедимся, что пользователь совпадает

        taskService.updateTask(task);  // Сохраняем изменения
        return "redirect:/tasks/today";  // Перенаправляем на список задач
    }

    @PostMapping("/updateCompleted/{id}")
    @ResponseBody
    public ResponseEntity<String> updateCompleted(@PathVariable("id") Long id, @RequestParam("completed") Boolean completed) {
        Optional<Task> taskOptional = Optional.ofNullable(taskService.getTaskById(id));
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setCompleted(completed); // Устанавливаем значение completed
            taskService.updateTask(task); // Используем метод обновления
            return ResponseEntity.ok("Задача обновлена"); // Возвращаем успешный ответ с текстом
        }
        return ResponseEntity.status(404).body("Задача не найдена"); // Если запись не найдена
    }


    // Удаление задачи
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks/today";  // Перенаправление на страницу задач на сегодня после удаления
    }

    // Статистика для задач на день! (Обновление в реальном времени AJAX!)
    @GetMapping("/today/stats/{date}")
    @ResponseBody
    public ResponseEntity<?> getTaskStats(@PathVariable("date") String date,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
//        LocalDate today = LocalDate.now();
        // Преобразуем строку в LocalDate
        LocalDate parsedDate = LocalDate.parse(date);
        List<Task> tasks = taskService.getTasksForDate(user, parsedDate);

        // Подсчёт статистики
        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(Task::isCompleted).count();
        long uncompletedTasks = totalTasks - completedTasks;

        // Вычисляем процент выполненных задач
        double completionPercentage = totalTasks == 0 ? 0 : (double) completedTasks / totalTasks * 100;

        // Формируем ответ
        Map<String, Object> stats = Map.of(
                "totalTasks", totalTasks,
                "completedTasks", completedTasks,
                "uncompletedTasks", uncompletedTasks,
                "completionPercentage", completionPercentage
        );

        return ResponseEntity.ok(stats);
    }


    @GetMapping("/calendar/{yearMonth}")
    public String viewCalendar(@PathVariable("yearMonth") String yearMonth,
                               Model model,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        YearMonth yearMonthObj = YearMonth.parse(yearMonth);
        LocalDate firstDayOfMonth = yearMonthObj.atDay(1);
        LocalDate lastDayOfMonth = yearMonthObj.atEndOfMonth();

        User user = userDetails.getUser();
        List<Task> tasks = taskService.getTasksForDateRange(user, firstDayOfMonth, lastDayOfMonth);

        // Группируем задачи по дате
        Map<LocalDate, List<Task>> tasksByDate = tasks.stream()
                .collect(Collectors.groupingBy(Task::getTaskDate));

        // Формируем данные для календаря
        List<List<LocalDate>> weeks = new ArrayList<>();
        LocalDate current = firstDayOfMonth.with(DayOfWeek.MONDAY);

        while (!current.isAfter(lastDayOfMonth)) {
            List<LocalDate> week = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                week.add(current);
                current = current.plusDays(1);
            }
            weeks.add(week);
        }

        System.out.println(weeks);
        System.out.println(tasksByDate);

        // Добавляем данные в модель
        model.addAttribute("weeks", weeks);
        model.addAttribute("tasksByDate", tasksByDate);
        model.addAttribute("currentMonth", yearMonthObj);
        model.addAttribute("previousMonth", yearMonthObj.minusMonths(1));
        model.addAttribute("nextMonth", yearMonthObj.plusMonths(1));

        return "calendar"; 
    }

}
