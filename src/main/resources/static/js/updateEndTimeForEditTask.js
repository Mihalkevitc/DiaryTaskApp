// Скрипт для проверки времени перед отправкой формы
function validateTime() {
    const startTimeInput = document.getElementById('startTime');
    const endTimeInput = document.getElementById('endTime');

    // Получаем значения времени
    const startTime = startTimeInput.value;
    const endTime = endTimeInput.value;

    // Проверка: если время окончания меньше времени начала
    if (startTime && endTime && endTime <= startTime) {
        alert('Время окончания не может быть меньше или равно времени начала.');
        return false; // Отменяем отправку формы
    }

    return true; // Форма отправляется
}

// Подключаем проверку перед отправкой формы
document.querySelector('form').onsubmit = function(event) {
    if (!validateTime()) {
        event.preventDefault(); // Предотвращаем отправку формы
    }
};


<!-- Скрипт для проверки времени перед отправкой формы -->

    // Проверка времени перед отправкой формы
    function validateTime() {
        const startTimeInput = document.getElementById('startTime');
        const endTimeInput = document.getElementById('endTime');

        // Получаем значения времени
        const startTime = startTimeInput.value;
        const endTime = endTimeInput.value;

        // Проверка: если время окончания меньше времени начала
        if (startTime && endTime && endTime <= startTime) {
            document.getElementById('warningMessage').style.display = 'block'; // Показываем предупреждение
            return false; // Отменяем отправку формы
        }

        document.getElementById('warningMessage').style.display = 'none'; // Скрываем предупреждение
        return true; // Форма отправляется
    }

    // Подключаем проверку перед отправкой формы
    document.querySelector('form').onsubmit = function(event) {
        if (!validateTime()) {
            event.preventDefault(); // Предотвращаем отправку формы
        }
    };

