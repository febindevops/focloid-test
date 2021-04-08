FROM openjdk:11
COPY borrower-service/target/borrower-service-0.0.1-SNAPSHOT.jar app.jar
COPY bin/wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "mysql-master:3306","-t","120", "--", "java", "-jar", "app.jar"]