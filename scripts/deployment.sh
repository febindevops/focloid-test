#!/bin/bash

cd /home/deployment/focloid-test

git pull

cd borrower-service

sudo mvn clean install

service payroll stop

cp -pr /home/deployment/focloid-test/borrower-service/target/borrower-service-0.0.1-SNAPSHOT.jar /opt/installer/payroll/app.jar

service payroll start
