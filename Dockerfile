# Stage 1: Build Stage
FROM maven:3.9.8-eclipse-temurin-21-alpine AS build

WORKDIR /project

COPY . .

RUN mvn clean package -DskipTests && \
    rm -rf /root/.m2

# Stage 2: Run Stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -g 1001 -S parcellegroup && \
    adduser -S plot -u 1001 -G parcellegroup &&\
    mkdir -p logs

COPY --from=build /project/target/*.jar plot.jar

COPY keystore.p12 /app/keystore.p12

RUN chown -R plot:parcellegroup logs && \
    chmod -R 755 logs && \
    chown plot:parcellegroup plot.jar && \
    chmod 755 plot.jar

USER plot

EXPOSE 8080

CMD ["java", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "-jar", "plot.jar", "--spring.profiles.active=vps"]
