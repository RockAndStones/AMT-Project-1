# AMT Project 1 : StoneOverflow

## Table des matières
- [Introduction](#Introduction)  
- [Installation](#Installation)

## Introduction

As part of the course AMT we were asked to create a site that will be a simple version of Stack Overflow. We will have to use the Jakarat EE technology to be able to create a site using Java language.

We will also use JUnit for the UnitTest and Codecept.js for the e2e tests.

## Installation

The site can be installed in different ways according to your preference.

First you can install it with a payara server for that you will have to do the following steps :

1. Clone the project using the command git clone :  
  `git clone git@github.com:RockAndStones/AMT-Project-1.git`

2. Go in the directory `docker/images/stoneoverflow` and run the script `build-image.sh`.  
    This script will make an **mvn clean package** on the project and copy the .war in the current directory and then build an docker image named **amt/stoneoverflow**

3. Go to the directory `docker/topologies/test` and run the command :  
   `docker compose up`  
   This command will start the payara server and you will now be able to access the application threw the port `8080` of your localhost or the ip address of your docker machine(`192.168.99.100:8080` for instance).

If you prefer OpenAffect you can also install it be doing the following steps:

Working on it...

Lastly, you can directly download a docker image of your application :

Working on it... 
