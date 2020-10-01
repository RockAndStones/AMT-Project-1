# AMT Project 1 : StoneOverflow

## Table des matières
- [Introduction](#Introduction)  
- [Installation](#Installation)

## Introduction

As part of the course AMT we were asked to create a site that will be a simple version of Stack Overflow. We will have to use the Jakarat EE technology to be able to create a site using Java language.

We will also use JUnit for the UnitTest and Codecept.js for the e2e tests.

If you want to see the specification of the user interface please [click here](https://docs.google.com/document/d/1DSahosKDQq_0yjQDg7r0EOaPcs6QhwXc7yyWqTjHFSo)

## Installation

The site can be installed in two different ways according to your preference.

First you can install it with an OpenLiberty server for that you will have to do the following steps :

1. Clone the project using the command git clone :  
  `git clone git@github.com:RockAndStones/AMT-Project-1.git`

2. Go in the directory `docker/images/stoneoverflow` and run the script `build-image.sh`.  
    This script will make an **mvn clean package** on the project and copy the **.war** in the current directory(`docker/images/stoneoverflow`) and then build a docker image named **liberty/stoneoverflow**.  
    It will also copy the configuration of the liberty server stores in `src/main/liberty`

3. Go to the directory `docker/topologies/test` and run the command : `docker compose up`  
   This command will start the OpenLiberty server and you will now be able to access the application through the port `9080` or `9443` for HTTPS of your **localhost** or the **ip address** of your docker machine(`192.168.99.100:8080` for instance).

The second way is the one when you can directly download a docker image with the package available on github :

For this one simply run the following command `docker pull ghcr.io/rockandstones/stoneoverflow:latest`
Then, run the command `docker run -p 9080:9080 -p 9443:94443 ghcr.io/rockandstones/stoneoverflow`.  
After that you can see the website using the same method as before.
