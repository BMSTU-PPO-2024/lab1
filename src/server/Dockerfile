FROM gradle:8.7.0-jdk11

COPY . /usr/src/server

RUN cd /usr/src/server && tr -d '\015' <build.sh >build.tmp && mv build.tmp build.sh && chmod +x build.sh && ./build.sh

WORKDIR "/usr/src/server"

CMD ["java", "-jar", "build/libs/server.jar"]
