FROM maven as build

WORKDIR /app/JAVA_FSD_BE

COPY . .

RUN mvn clean package

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/ifarmr/target/iFarmr-Application-0.0.1-SNAPSHOT.jar IFarmrApplication.jar

ENTRYPOINT ["java","-jar","iFarmr-Application-0.0.1-SNAPSHOT.jar"]