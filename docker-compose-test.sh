#!/bin/bash
source ./docker.properties
export PROFILE=docker

echo '### Java version ###'
java --version

front="./rangiffler-gql-client/";
front_image="${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-${PROFILE}:latest";
docker_arch=""

ARCH="$docker_arch" FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" PROFILE="${PROFILE}" docker-compose -f docker-compose.test.yml down

ARCH=$(uname -m)

bash ./gradlew -Pskipjaxb jibDockerBuild -x :rangiffler-e2e-tests:test

if [ "$ARCH" = "arm64" ] || [ "$ARCH" = "aarch64" ]; then
  docker_arch="linux/arm64"
  docker build --build-arg DOCKER=arm64v8/eclipse-temurin:19-jdk -t "${IMAGE_PREFIX}/${TEST_IMAGE_NAME}:latest" -f ./rangiffler-e2e-tests/Dockerfile .
else
  docker_arch="linux/amd64"
  docker build --build-arg DOCKER=eclipse-temurin:19-jdk -t "${IMAGE_PREFIX}/${TEST_IMAGE_NAME}:latest" -f ./rangiffler-e2e-tests/Dockerfile .
fi

cd "$front" || exit
bash ./docker-build.sh ${PROFILE}
cd ../ || exit
docker pull selenoid/vnc_chrome:117.0
docker images
ARCH="$docker_arch" FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" PROFILE="${PROFILE}" docker-compose -f docker-compose.test.yml up -d
docker ps -a