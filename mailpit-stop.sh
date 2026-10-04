#!/bin/bash

set -e
set -o pipefail

# Stops the local catch-all mail server started by mailpit-start.sh.
# The container is created with --rm, so stopping it also removes it.

CONTAINER_NAME="foilen-studies-mailpit"

if [ -n "$(docker ps -q -f name="^${CONTAINER_NAME}$")" ]; then
  echo "Stopping ${CONTAINER_NAME} container"
  docker stop "${CONTAINER_NAME}"
else
  echo "${CONTAINER_NAME} container is not running"
fi
