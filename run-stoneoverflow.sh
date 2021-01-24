#!/bin/bash
# Stop any existing liberty instance
mvn liberty:stop
# Run container dependencies
cd docker
docker-compose stop
docker-compose rm -f
docker-compose up -d stoneoverflow-db gamification-api
# Wait for gamification api to be ready
while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' localhost:8081/swagger-ui/)" != "200" ]]; do sleep 5; done
# Run StoneOverflow
docker-compose up -d stoneoverflow-webapp
