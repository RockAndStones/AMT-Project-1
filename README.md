# AMT Project 1 : StoneOverflow <img src="https://github.com/RockAndStones/AMT-Project-1/workflows/MVN%20&%20E2E%20Tests/badge.svg?branch=dev" alt="Tests">

## Table of contents
- [Introduction](#Introduction)  
- [Sql Schema](#Sql-Schema)  
- [Deployment](#Deployment)
- [Tests](#Tests)

## Introduction
As part of the course AMT we were asked to create a site that will be a simple version of Stack Overflow. We will have to use the Jakarat EE technology to be able to create a site using Java language.

We will also use JUnit for the UnitTest and Codecept.js for the e2e tests.

If you want to see the specification of the user interface please [click here](https://docs.google.com/document/d/1DSahosKDQq_0yjQDg7r0EOaPcs6QhwXc7yyWqTjHFSo/edit?usp=sharing)

## Sql Schema

The project contains a sql database and below you will see the different tables and views used in this database.

![Sql Model](./img/SqlModel.PNG)

## Deployment
The web application can be deployed with a simple [docker-compose](./docker/docker-compose.yml) file.

First clone the repository.
```
git clone https://github.com/RockAndStones/AMT-Project-1.git
```
Next open a terminal in the docker folder of the cloned repository and run the following command. 
```
docker-compose up -d
```
You will then be able to access the web application at the url http://localhost:8080 when docker-compose is up and running.

## Tests
If you want to run the tests you will first need start the docker database container.

First clone the repository.
```
git clone https://github.com/RockAndStones/AMT-Project-1.git
```
Then run the docker database container with the docker-compose file available in docker folder.
```
cd ./AMT-Project-1/docker
docker-compose -d sql
```
### Unit & Integration Tests
To run the unit & integration tests, use the `run-unit-integration-tests.sh` script from the cloned root folder.
### End to End (E2E) Tests
To run the end to end (E2E) tests, use the `run-e2e-tests.sh` script from the cloned root folder.
