#!/bin/bash
# Stop any existing liberty instance
mvn liberty:stop
# Run container dependencies
cd docker
docker-compose stop
docker-compose rm -f
docker-compose up -d stoneoverflow-db gamification-api
cd ..
# Update environment properties
sed -i '/DB_HOST/s/=.*/=localhost/' src/main/liberty/config/server.env
sed -i '/ch\.heigvd\.amt\.gamification\.server\.url/s/=.*/=http\:\/\/localhost\:8081/' src/main/resources/environment.properties
# Prepare liberty server
mvn clean package
mvn liberty:create liberty:install-feature liberty:deploy
# Wait for gamification api to be ready
./docker/wait-for-it.sh --timeout=0 localhost:8081
./docker/wait-for-it.sh --timeout=0 localhost:3306
# Run liberty server
mvn liberty:start
# Access StoneOverflow home page to generate default data
curl http://localhost:8080/home
# Run end to end tests
cd e2e
npm install
npx codeceptjs run --colors --steps
# Stop liberty server and containers
cd ..
mvn liberty:stop
cd docker
docker-compose down
cd ..
# Restore environment properties
sed -i '/DB_HOST/s/=.*/=stoneoverflow-db/' src/main/liberty/config/server.env
sed -i '/ch\.heigvd\.amt\.gamification\.server\.url/s/=.*/=http\:\/\/gamification\-api\:8080/' src/main/resources/environment.properties
