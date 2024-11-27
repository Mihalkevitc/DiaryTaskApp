// Находим все звёздочки на странице (тут в отличие от других есть и id записей, и не нужен input
function setImportance(event) {
    const star = event.target; // Получаем элемент звезды
    const entryId = star.getAttribute("data-entry-id"); // Идентификатор записи
    const importance = star.getAttribute("data-star"); // Уровень важности

    // Проверяем значения в консоли
    console.log("entryId:", entryId);
    console.log("importance:", importance);

    // Отправляем запрос на сервер
    fetch(`/diary/updateImportance/${entryId}?importance=${importance}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
    .then((response) => {
        if (response.ok) {
            // Обновляем отображение звёздочек
            updateStars(entryId, importance);
        }
    })
    .catch((error) => {
        console.error('Ошибка:', error);
    });
}

// Обновление отображения звёздочек
function updateStars(entryId, importance) {
    const stars = document.querySelectorAll(`.star[data-entry-id="${entryId}"]`);
    stars.forEach((star) => {
        const starValue = star.getAttribute("data-star");
        if (starValue <= importance) {
            star.classList.add("filled-star");
            star.classList.remove("empty-star");
        } else {
            star.classList.add("empty-star");
            star.classList.remove("filled-star");
        }
    });
}

// Назначаем обработчики событий для всех звёздочек после загрузки страницы
document.addEventListener("DOMContentLoaded", () => {
    const stars = document.querySelectorAll(".star");
    stars.forEach((star) => {
        star.addEventListener("click", setImportance);
    });
});
