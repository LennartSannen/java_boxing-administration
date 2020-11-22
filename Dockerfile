FROM openjdk:8
COPY ./target/app.jar /
EXPOSE 8080
CMD java -jar app.jar