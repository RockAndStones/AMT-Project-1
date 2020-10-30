#!/bin/bash
mvn liberty:stop
mvn clean package
mvn liberty:create liberty:install-feature liberty:deploy
mvn liberty:start
cd e2e
npm install
npx codeceptjs run --colors --steps
read -p "Press any key to quit ..."
