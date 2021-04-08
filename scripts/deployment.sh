#!/bin/bash

sudo cd /home/deployment/focloid-test

sudo git pull

sudo cd borrower-service && sudo mvn clean install

sudo service payroll stop

sudo cp -pr target/borrower-service-0.0.1-SNAPSHOT.jar /opt/installer/payroll/

sudo service payroll start
