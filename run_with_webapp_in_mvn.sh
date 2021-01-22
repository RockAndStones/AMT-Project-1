#!/bin/bash
cd docker
docker-compose down
docker-compose build gamification-db gamification-api stoneoverflow-db
docker-compose up -d stoneoverflow-db gamification-api
cd ..
mvn liberty:stop
mvn clean package
mvn liberty:dev
mvn liberty:stop
