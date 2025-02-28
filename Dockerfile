FROM maven:3.6.3-openjdk-17-slim AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:jdk-oracle

COPY  --from=build app/target/task-management-system-0.0.1-SNAPSHOT.jar /opt/task-management-system-0.0.1-SNAPSHOT.jar

CMD ["java", "-Dspring.profiles.active=cloud", "-jar", "/opt/task-management-system-0.0.1-SNAPSHOT.jar"]

EXPOSE 8088