# AMT Project 1 : StoneOverflow

## Table of contents
- [Introduction](#Introduction)  
- [Installation](#Installation)

## Introduction
As part of the course AMT we were asked to create a site that will be a simple version of Stack Overflow. We will have to use the Jakarat EE technology to be able to create a site using Java language.

We will also use JUnit for the UnitTest and Codecept.js for the e2e tests.

If you want to see the specification of the user interface please [click here](https://docs.google.com/document/d/1DSahosKDQq_0yjQDg7r0EOaPcs6QhwXc7yyWqTjHFSo/edit?usp=sharing)

## Deployment
The web application can be deployed with a simple [docker-compose](./docker/docker-compose.yml) file downloadable [here](https://githubraw.com/RockAndStones/AMT-Project-1/master/docker/docker-compose.yml).

Next open a terminal where the docker-compose file is and run the following command. 
```
docker-compose up -d
```
You will then be able to access the web application at the url http://localhost:8080 when docker-compose is up and running.

## Running Tests
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
To run the unit & integration tests, use the `run-unit-integration-tests.sh` script from the clone root folder.
### End to End (E2E) Tests
To run the end to end (E2E) tests, use the `run-e2e-tests.sh` script from the clone root folder.
