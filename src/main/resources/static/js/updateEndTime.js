// Скрипт для обновления минимального времени завершения
function updateEndTimeMin() {
    const startTimeInput = document.getElementById('startTime');
    const endTimeInput = document.getElementById('endTime');
    endTimeInput.min = startTimeInput.value;
}