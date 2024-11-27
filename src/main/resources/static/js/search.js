function searchEntries() {
    const query = document.getElementById('query').value;
    const importance = document.getElementById('importance').value;

    fetch(`/diary/search?query=${query}&importance=${importance}`)
        .then(response => response.json())
        .then(data => {
            const entriesList = document.getElementById('entries-list');
            entriesList.innerHTML = '';  // Очищаем список

            // Генерируем HTML для каждого найденного результата
            data.entries.forEach(entry => {
                const listItem = document.createElement('li');
                listItem.innerHTML = `
                    <h3>${entry.title}</h3>
                    <p>${entry.content}</p>
                    <p>Важность: ${entry.importance}</p>
                `;
                entriesList.appendChild(listItem);
            });
        })
        .catch(error => console.error('Ошибка при поиске:', error));
}
