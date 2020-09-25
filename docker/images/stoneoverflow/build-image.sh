#!/bin/bash

mvn clean package -f ../../../
cp ../../../target/StoneOverflow-1.0-SNAPSHOT.war ./StoneOverflow.war
docker build -t amt/stoneoverflow .
