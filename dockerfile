FROM openjdk:latest
COPY ./build/libs/warehouse-0.0.1-SNAPSHOT.jar /warehouse.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "warehouse.jar"]