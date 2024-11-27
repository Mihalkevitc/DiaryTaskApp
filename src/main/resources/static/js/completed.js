document.querySelectorAll('.task-checkbox').forEach(checkbox => {
    checkbox.addEventListener('change', function () {
        const taskId = this.dataset.taskId;
        const completed = this.checked;

        fetch(`/tasks/updateCompleted/${taskId}?completed=${completed}`, {
            method: 'POST',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка при обновлении задачи');
                }
                // После обновления задачи обновляем статистику
                updateTaskStats();
            })
            .catch(error => console.error('Ошибка:', error));
    });
});


    //Вот это, чтобы противный булиан был ьулиано, а не стринг!!!))
    document.addEventListener("DOMContentLoaded", function () {
    // Получаем все чекбоксы с классом task-checkbox
    const checkboxes = document.querySelectorAll(".task-checkbox");

    checkboxes.forEach(checkbox => {
        // Преобразуем строку в булево значение
        const completed = checkbox.getAttribute("data-completed") === "true";
        console.log(`Задача: ${checkbox.getAttribute("data-task-id")}, Completed: ${completed}`);
    });
    });