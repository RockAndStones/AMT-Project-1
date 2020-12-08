#!/bin/bash
mvn liberty:stop
cd docker
docker-compose up -d sql
cd ..
mvn clean package
mvn liberty:create liberty:install-feature liberty:deploy
mvn liberty:start
curl http://localhost:8080/home
mvn verify
cd docker
docker-compose down
read -p "Press any key to quit ..."

