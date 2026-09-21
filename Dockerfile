# Dockerfile (đặt ở root của repo Java-Framework)
FROM maven:3.9-eclipse-temurin-21

WORKDIR /fw

# Copy pom.xml trước để cache layer dependency resolve
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source rồi build + install vào .m2
COPY src ./src
RUN mvn clean install -DskipTests
