FROM maven:3.9.4-eclipse-temurin-21-alpine as build

WORKDIR /java

COPY . /java

RUN mvn clean package -Dmaven.test.skip=true

FROM gradle:jdk21-alpine

COPY --from=build /java/target/task-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
