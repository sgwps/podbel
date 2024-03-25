FROM openjdk:17

WORKDIR /app

COPY target/*.jar app.jar

COPY src/main/resources/subjects.csv /app/subjects.csv
COPY src/main/resources/tasks.json /app/tasks.json
COPY src/main/resources/max_module.sql /app/max_module.sql

COPY src/main/resources/templates /app/resources/templates

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]

#FROM tomcat
#WORKDIR /app
#COPY target/*.war /usr/local/tomcat/webapps/
#
#COPY src/main/resources/subjects.csv /usr/local/tomcat/webapps/subjects.csv
#COPY src/main/resources/tasks.json /usr/local/tomcat/webapps/tasks.json
#COPY src/main/resources/max_module.sql /usr/local/tomcat/webapps/max_module.sql
#
#EXPOSE 8080
#ENTRYPOINT ["catalina.sh", "run"]