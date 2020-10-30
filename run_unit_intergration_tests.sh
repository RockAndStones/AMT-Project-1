#!/bin/bash
mvn liberty:stop
mvn clean package
mvn liberty:create liberty:install-feature liberty:deploy
mvn liberty:start
curl http://localhost:8080/home
mvn verify
read -p "Press any key to quit ..."

