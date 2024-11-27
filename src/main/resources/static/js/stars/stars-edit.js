//Тут нужно найти крытое поле ввода для уровня важности (кнопка сохранить запись требует input)
document.addEventListener('DOMContentLoaded', function () {
    // Находим все элементы звезды на странице
    const stars = document.querySelectorAll('.star');

    // Находим скрытое поле ввода для уровня важности
    const importanceInput = document.getElementById('importance');

    // Для каждой звезды назначаем обработчик события клика
    stars.forEach(star => {
        star.addEventListener('click', function () {
            // Получаем идентификатор записи из атрибута data-entry-id
            const entryId = this.getAttribute('data-entry-id');

            // Извлекаем значение уровня важности из атрибута data-star для текущей звезды
            const starValue = parseInt(this.getAttribute('data-star'));

            // Обновляем значение скрытого поля ввода уровня важности с выбранным значением звезды
            importanceInput.value = starValue;

            // Обновляем отображение звездочек
            stars.forEach(starElement => {
                // Получаем значение уровня важности для каждой звезды
                const currentStarValue = parseInt(starElement.getAttribute('data-star'));

                // Если текущая звезда имеет уровень важности меньше или равен выбранному
                if (currentStarValue <= starValue) {
                    // Добавляем класс для отображения заполненной звезды и убираем класс для пустой
                    starElement.classList.add('filled-star');
                    starElement.classList.remove('empty-star');
                } else {
                    // Если уровень важности текущей звезды больше выбранного, убираем класс для заполненной звезды и добавляем класс для пустой
                    starElement.classList.remove('filled-star');
                    starElement.classList.add('empty-star');
                }
            });

            // Отправляем запрос на сервер для обновления важности
            fetch(`/diary/updateImportance/${entryId}?importance=${starValue}`, {
                method: 'POST'
            }).then(response => {
                if (response.ok) {
                    console.log('Important rating updated successfully');
                } else {
                    console.error('Failed to update importance');
                }
            });
        });
    });
});
