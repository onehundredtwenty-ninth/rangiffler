#!/bin/bash
source ../docker.properties

echo '### Build dev frontend GQL image ###'
echo "run command: build --build-arg NPM_COMMAND=${NPM_DOCKER_BUILD} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-"$1":${FRONT_VERSION} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-"$1":latest ."
docker build --build-arg NPM_COMMAND=${NPM_DOCKER_BUILD} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-"$1":${FRONT_VERSION} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-"$1":latest .

if [ "$2" = "push" ]; then
  echo '### Push frontend GQL image ###'
  docker push ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-"$1":${FRONT_VERSION}
  docker push ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-"$1":latest
fi
