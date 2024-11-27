@GetMapping("/calendar")
public String viewCalendar(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
    User user = userDetails.getUser();
    LocalDate today = LocalDate.now();

    // Получаем первый и последний день текущего месяца
    LocalDate firstDayOfMonth = today.withDayOfMonth(1);
    LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

    // Получаем все задачи за месяц
    List<Task> tasks = taskService.getTasksForMonth(user, firstDayOfMonth, lastDayOfMonth);

    // Генерируем список недель (календарь)
    List<List<Day>> calendar = generateCalendar(firstDayOfMonth, tasks);
    model.addAttribute("calendar", calendar);

    return "calendar"; // Шаблон для отображения календаря
}

private List<List<Day>> generateCalendar(LocalDate startOfMonth, List<Task> tasks) {
    List<List<Day>> weeks = new ArrayList<>();
    LocalDate date = startOfMonth;
    List<Day> week = new ArrayList<>();

    while (date.getMonth() == startOfMonth.getMonth()) {
        int taskCount = (int) tasks.stream().filter(task -> task.getTaskDate().equals(date)).count();
        week.add(new Day(date, taskCount));

        if (week.size() == 7) {
            weeks.add(week);
            week = new ArrayList<>();
        }

        date = date.plusDays(1);
    }

    // Добавляем пустые дни, если месяц не начинается с понедельника
    if (!week.isEmpty()) {
        weeks.add(week);
    }

    return weeks;
}

public static class Day {
    private LocalDate date;
    private int tasks;

    public Day(LocalDate date, int tasks) {
        this.date = date;
        this.tasks = tasks;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTasks() {
        return tasks;
    }
}
