# ---------- BUILD STAGE ----------
FROM maven:latest AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и загружаем зависимости
COPY pom.xml .

# Собираем зависимости (перед сборкой проекта для ускорения процесса)
#RUN mvn dependency:go-offline

# Копируем весь проект в контейнер
COPY src ./src

# Собирать проект в jar файл
RUN mvn clean package -DskipTests

# Используем базовый образ для выполнения
FROM amazoncorretto:21-alpine-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar файл из предыдущего этапа
COPY --from=build /app/target/auth-0.0.1-SNAPSHOT.jar auth.jar

# ---------- ENV variables ----------
ENV DB_HOST=host.docker.internal
ENV DB_PORT=5433
ENV DB_USERNAME=user
ENV DB_PASSWORD=password
ENV DB_NAME=auth_db

# ---------- Ports ----------
EXPOSE 8081

# ---------- Run ----------
ENTRYPOINT ["java", "-jar", "auth.jar"]
