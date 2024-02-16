#!/bin/bash

## This script should be run from the root of the project like this:
##
##    $ ./deployment/scripts/deploy_to_server.sh
##


## Check if .jar file exists
if [! -f "services/rest/target/calendar-services-rest-0.0.1-SNAPSHOT.jar" ]; then
  echo "Compiled .jar file does not exist, so nothing to deploy!"
  echo "Please run `mvn clean package -DskipTests` in order to build .jar file and rerun this command."
  exit 1
fi

## Run Docker build command to build Docker image
docker build .

## Copy Docker image (maybe save it first?) to server
# TODO Copy Docker image to server...

## Connect to server
# TODO Connect to server...

## Run Docker Compose to spin up the application and environment
# TODO Run Docker Compose to spin up application and environment...

echo "Application has successfully been deployed to server"


echo "NOTICE: This command will not work as expected until the above TODOs have been fixed. Remove this line when all TODOs have been fixed."