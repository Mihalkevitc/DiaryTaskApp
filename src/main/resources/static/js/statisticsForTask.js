// Функция для запуска конфетти
function launchConfetti() {
    confetti({
        particleCount: 300, // Количество частиц
        spread: 170,         // Ширина распыления
        origin: { y: 0.6 }, // Где будет происходить распыление (в 60% от высоты страницы)
        colors: ['#ff6b6b', '#feca57', '#1dd1a1', '#48dbfb', '#ff9ff3'], // Цвета конфетти
    });
}


// Функция для обновления статистики
function updateTaskStats() {
    // Получаем элемент с id "currentDate" и считываем атрибут data-current-date
    const currentDate = document.getElementById('currentDate').getAttribute('data-current-date');

    // Теперь переменная currentDate содержит значение, переданное из Thymeleaf
//    console.log(typeof(currentDate));
//    console.log(currentDate);

    // При передачи параметра в запрос надо интерполировать строку, то есть ставить обратные кавычки
    // Тогда строка горит не зелёным а белым, как сейчас!
    fetch(`/tasks/today/stats/${currentDate}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка при получении статистики задач');
            }
            return response.json();
        })
        .then(data => {
            // Обновляем данные в DOM
            document.getElementById('totalTasks').textContent = data.totalTasks;
            document.getElementById('completedTasks').textContent = data.completedTasks;
            document.getElementById('uncompletedTasks').textContent = data.uncompletedTasks;

            // Вычисляем процент выполнения
            const completionPercentage = data.completionPercentage.toFixed(0);
            document.getElementById('completionPercentage').textContent = completionPercentage; // Текстовое отображение
            document.getElementById('progressBar').value = completionPercentage; // Прогресс-бар

            // Мотивационное сообщение с эмодзи
            let message = "";
            if (completionPercentage <= 20) {
                message = "Не теряй время, давай начни с самых простых задач! ⏳😅";
            } else if (completionPercentage <= 40) {
                message = "Хороший старт! Продолжай в том же духе. 💪🙂";
            } else if (completionPercentage <= 60) {
                message = "Половина пути пройдена! Ты на верном пути! 🚀👍";
            } else if (completionPercentage <= 80) {
                message = "Отлично! Ты почти у цели! 🏅😎";
            } else {
                message = "Ты супер!!!:) 🎉🏆";
            }

            // Обновляем сообщение
            document.getElementById('motivationMessage').textContent = message;

            // Если все задачи выполнены, запускаем эффект конфетти
            if (data.totalTasks > 0 && data.completedTasks === data.totalTasks) {
                launchConfetti(); // Запуск конфетти
            }
        })
        .catch(error => console.error('Ошибка:', error));
}

// Первоначальное обновление
updateTaskStats();

