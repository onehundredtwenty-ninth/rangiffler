ARG DOCKER
FROM $DOCKER
ENV JAVA_OPTS=""

WORKDIR /rangiffler
COPY ./gradle ./gradle
COPY ./gradlew ./
COPY ./rangiffler-e2e-tests ./rangiffler-e2e-tests
COPY ./rangiffler-grpc-common ./rangiffler-grpc-common
COPY ./build.gradle ./
COPY ./settings.gradle ./

CMD ./gradlew test -Dtest.env=docker