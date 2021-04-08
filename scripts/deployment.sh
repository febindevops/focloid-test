#!/bin/bash

cd /home/deployment/focloid-test

git pull

cd borrower-service

mvn clean install

service payroll stop

cp -pr target/borrower-service-0.0.1-SNAPSHOT.jar /opt/installer/payroll/

service payroll start
