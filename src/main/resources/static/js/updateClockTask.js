// Функция для форматирования времени (часы:минуты:секунды)
        function formatTime(date) {
            let hours = date.getHours();
            let minutes = date.getMinutes();
            let seconds = date.getSeconds();

            // Добавляем 0 для чисел меньше 10
            hours = hours < 10 ? '0' + hours : hours;
            minutes = minutes < 10 ? '0' + minutes : minutes;
            seconds = seconds < 10 ? '0' + seconds : seconds;

            return `${hours}:${minutes}:${seconds}`;
        }

        // Функция для обновления текущего времени
        function updateTime() {
            const currentTime = document.getElementById('current-time');
            const date = new Date();
            currentTime.textContent = formatTime(date);
        }

        // Запускаем обновление времени каждую секунду
        setInterval(updateTime, 1000);

        // Первоначальное обновление
        updateTime();