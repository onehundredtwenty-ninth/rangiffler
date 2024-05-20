#!/bin/bash
source ./docker.properties
export PROFILE="${PROFILE:=docker}"

echo '### Java version ###'
java --version

front_path="./${FRONT_IMAGE_NAME_GQL}/";
front_image="${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-${PROFILE}:latest";

FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" PROFILE="${PROFILE}" docker-compose down

if [ "$1" = "push" ] || [ "$2" = "push" ]; then
  echo "### Build & push images (front_path: $front_path) ###"
  bash ./gradlew -Pskipjaxb jib
  cd "$front_path" || exit
  bash ./docker-build.sh ${PROFILE} push
else
  echo "### Build images (front_path: $front_path) ###"
  echo "### FRONT_IMAGE (FRONT_IMAGE_NAME: $front_image) ###"
  bash ./gradlew -Pskipjaxb jibDockerBuild
  cd "$front_path" || exit
  bash ./docker-build.sh ${PROFILE}
fi

cd ../
docker images
FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" PROFILE="${PROFILE}" docker-compose up -d
docker ps -a
