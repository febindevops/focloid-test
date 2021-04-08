#!/bin/bash

cd /home/deployment/focloid-test

sudo git pull

cd borrower-service && sudo mvn clean install

sudo service payroll stop

sudo cp -pr /home/deployment/focloid-test/borrower-service/target/borrower-service-0.0.1-SNAPSHOT.jar /opt/installer/payroll/app.jar

sudo service payroll start
