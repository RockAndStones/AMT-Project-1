#!/bin/bash
# Stop any existing liberty instance
mvn liberty:stop
# Run container dependencies
cd docker
docker-compose up -d stoneoverflow-db gamification-api
# Wait for gamification api to be ready
./wait-for-it.sh --timeout=0 localhost:8081
# Run StoneOverflow
docker-compose up -d stoneoverflow-webapp