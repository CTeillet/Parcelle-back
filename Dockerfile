# Stage 1: Build Stage
FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /project

COPY . .

RUN mvn clean package -DskipTests && \
    rm -rf /root/.m2

# Stage 2: Run Stage
FROM openjdk:20-jre-slim

WORKDIR /app

RUN addgroup -g 1001 -S parcellegroup && \
    adduser -S parcelle -u 1001 -G parcellegroup

COPY --from=build /project/target/*.jar .

RUN chown -R parcelle:parcellegroup /app

USER parcelle

CMD ["java", "-jar", "*.jar"]
