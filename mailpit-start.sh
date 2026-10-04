#!/bin/bash

set -e
set -o pipefail

# Local catch-all mail server for development.
#
# Every email sent by the app (login codes) is captured here instead of being delivered.
# Use these in application.properties: spring.mail.host=localhost and spring.mail.port=1026
#
#   Web UI : http://localhost:8025/
#   SMTP   : localhost:1026

CONTAINER_NAME="foilen-studies-mailpit"

if [ -n "$(docker ps -aq -f name="^${CONTAINER_NAME}$")" ]; then
  echo "Starting existing ${CONTAINER_NAME} container"
  docker start "${CONTAINER_NAME}"
else
  echo "Creating and starting ${CONTAINER_NAME} container"
  docker run -d \
    --name "${CONTAINER_NAME}" \
    --rm \
    -p 1026:1025 \
    -p 8025:8025 \
    -e MP_MAX_MESSAGES=5000 \
    -e MP_SMTP_AUTH_ACCEPT_ANY=true \
    -e MP_SMTP_AUTH_ALLOW_INSECURE=true \
    axllent/mailpit:latest
fi

echo "Mailpit web UI: http://localhost:8025/"
