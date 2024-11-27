# Этап сборки: используем Maven для сборки проекта
FROM maven:3.8-openjdk-17 AS build

# Копируем файлы проекта в контейнер
COPY ./ /usr/src/app

# Устанавливаем рабочую директорию
WORKDIR /usr/src/app

# Выполняем сборку Maven проекта
RUN mvn clean package -DskipTests

# Этап выполнения: используем OpenJDK для запуска
FROM openjdk:17-jdk-alpine

# Копируем jar-файл из первого этапа сборки
COPY --from=build /usr/src/app/target/*.jar /app/app.jar

# Открываем порт 8080 для приложения
EXPOSE 8080

# Команда для запуска приложения
CMD ["java", "-jar", "/app/app.jar"]
