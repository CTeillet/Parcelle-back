FROM maven:3.9.3-eclipse-temurin-20-alpine AS build

RUN mkdir /project

COPY . /project

WORKDIR /project

RUN mvn clean package -DskipTests

FROM eclipse-temurin:20-jdk-alpine

RUN mkdir /app

RUN addgroup -g 1001 -S parcellegroup

RUN adduser -S parcelle -u 1001

COPY --from=build /project/target/*.jar /app/parcelle.jar

WORKDIR /app

RUN chown -R parcelle:parcellegroup /app

CMD java $JAVA_OPTS -jar parcelle.jar
