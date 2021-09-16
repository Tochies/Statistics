FROM openjdk:11
ADD target/transaction-statistics.jar transaction-statistics.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "transaction-statistics.jar"]

# Before building the container image run:
#
# ./mvnw package
#
# Then, build the image with:
#  docker build -f Dockerfile -t transaction-statistics .
#
# Then run the container using:
#
# docker run -i --rm -p 8080:8080 quarkus/autotopup-sochitel-jvm
