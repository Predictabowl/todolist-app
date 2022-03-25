FROM openjdk:17-oracle

COPY /configuration-module/target/*.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]