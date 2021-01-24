#!/bin/bash
# Stop any existing liberty instance
mvn liberty:stop
# Run container dependencies
cd docker
docker-compose up -d stoneoverflow-db gamification-api
cd ..
# Prepare liberty server
mvn clean package
mvn liberty:create liberty:install-feature liberty:deploy
# Wait for gamification api to be ready
./docker/wait-for-it.sh --timeout=0 localhost:8081
# Run liberty server
mvn liberty:start
# Access StoneOverflow home page to generate default data
curl http://localhost:8080/home
# Run integration tests
mvn verify
# Stop liberty server and containers
mvn liberty:stop
cd docker
docker-compose down