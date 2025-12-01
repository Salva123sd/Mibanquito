FROM amazoncorretto:21-alpine-jdk

# Copia cualquier .jar de target y lo renombra
COPY target/ARFastCheck-0.0.1-SNAPSHOT.jar /mibanquito.jar


ENTRYPOINT ["java", "-jar", "/mibanquito.jar"]
