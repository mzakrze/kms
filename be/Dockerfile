FROM ubuntu:16.04

RUN apt-get update && \
      apt-get -y install sudo && \
      sudo apt-get -y install postgresql-client && \
      sudo apt-get -y install openjdk-8-jre

ARG JAR_FILE
COPY $JAR_FILE app.jar
CMD ["/bin/bash", "-c", "java -jar /app.jar --spring.config.location=file:/run/secrets/application.properties"]
# TODO - prawdopodobnie wynieść tego Dockerfile'a do innego katalogu