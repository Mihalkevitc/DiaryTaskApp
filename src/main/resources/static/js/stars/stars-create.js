// Функция для изменения важности (в отлчии от отсальных тут нет id записи, так ка она ещё не создана)
function setImportance(event) {
    // Получаем элемент звезды, на которую был произведен клик
    const star = event.target;

    // Извлекаем уровень важности из атрибута "data-star" элемента звезды
    const importance = star.getAttribute("data-star");

    // Проверяем значение уровня важности в консоли для отладки
    //console.log("importance:", importance);

    // Находим скрытое поле с id "importance" и обновляем его значением выбранного уровня важности
    const importanceField = document.getElementById("importance");
    importanceField.value = importance;

    // Находим все звезды на странице
    const stars = document.querySelectorAll(".star");

    // Проходим по всем звездам и обновляем их отображение в зависимости от выбранной важности
    stars.forEach((star) => {
        // Извлекаем уровень важности для текущей звезды
        const starValue = star.getAttribute("data-star");

        // Если уровень важности текущей звезды меньше или равен выбранному, то звезда должна быть заполненной
        if (starValue <= importance) {
            // Добавляем класс для отображения заполненной звезды и убираем класс для пустой
            star.classList.add("filled-star");
            star.classList.remove("empty-star");
        } else {
            // Если уровень важности текущей звезды больше выбранного, то звезда должна быть пустой
            // Добавляем класс для отображения пустой звезды и убираем класс для заполненной
            star.classList.add("empty-star");
            star.classList.remove("filled-star");
        }
    });
}

// Назначаем обработчики событий для всех звёздочек после полной загрузки страницы
document.addEventListener("DOMContentLoaded", () => {
    // Находим все элементы с классом "star" (все звезды)
    const stars = document.querySelectorAll(".star");

    // Для каждой звезды назначаем обработчик события клика, который вызовет функцию setImportance
    stars.forEach((star) => {
        star.addEventListener("click", setImportance);
    });
});

