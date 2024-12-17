FROM openjdk:17-jdk-slim

RUN mkdir /app

COPY ./ /app

WORKDIR /app

CMD java IFarmrApplication