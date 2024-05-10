FROM openjdk:11

COPY . /usr/src/server

RUN cd /usr/src/server && chmod +x gradlew && ./gradlew shadowJar

WORKDIR "/usr/src/server"

CMD ["java", "-jar", "build/libs/server.jar"]
