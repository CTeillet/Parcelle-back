# Stage 1: Build Stage
FROM maven:3.8.7-eclipse-temurin-17-alpine AS build

WORKDIR /project

COPY . .

RUN mvn clean package -DskipTests && \
    rm -rf /root/.m2

# Stage 2: Run Stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

#Create a repertory for the logs
RUN mkdir -p /app/logs && \
    chown -R 1001:1001 /app/logs && \
    chmod 755 /var/log/parcelle

RUN addgroup -g 1001 -S parcellegroup && \
    adduser -S parcelle -u 1001 -G parcellegroup

COPY --from=build /project/target/*.jar parcelle.jar

RUN chown parcelle:parcellegroup parcelle.jar && \
    chmod 755 parcelle.jar

USER parcelle

CMD ["java", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "-jar", "parcelle.jar", "--spring.profiles.active=vps"]
