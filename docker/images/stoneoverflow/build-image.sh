#!/bin/bash

mvn clean package -f ../../../
cp ../../../target/Project1-1.0-SNAPSHOT.war ./StoneOverflow.war
docker build -t amt/stoneoverflow .
