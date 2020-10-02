#!/bin/bash

mvn clean package -f ../../../
cp ../../../target/StoneOverflow.war ./StoneOverflow.war

docker build -t stoneoverflow/liberty .
