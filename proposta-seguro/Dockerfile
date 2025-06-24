FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/proposta-seguro-0.0.1-SNAPSHOT.jar propostaSeguro.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "propostaSeguro.jar"]
