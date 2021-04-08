#!/bin/bash

chown -R deployment:deployment /home/deployment/focloid-test

cd /home/deployment/focloid-test

git pull

chown -R deployment:deployment /home/deployment/focloid-test

cd borrower-service

sudo mvn clean install

service payroll stop

cp -pr /home/deployment/focloid-test/borrower-service/target/borrower-service-0.0.1-SNAPSHOT.jar /opt/installer/payroll/app.jar

service payroll start
