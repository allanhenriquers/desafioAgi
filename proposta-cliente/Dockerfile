FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/proposta-cliente-0.0.1-SNAPSHOT.jar apiCliente.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "apiCliente.jar"]
