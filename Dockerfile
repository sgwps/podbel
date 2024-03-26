FROM openjdk:17

WORKDIR /app

COPY target/*.jar app.jar

COPY src/main/resources/subjects.csv /app/subjects.csv
COPY src/main/resources/tasks.json /app/tasks.json
COPY src/main/resources/max_module.sql /app/max_module.sql

COPY src/main/resources/templates /app/templates

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]