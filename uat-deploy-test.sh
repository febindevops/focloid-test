(cd borrower-service/ && mvn clean package)
scp -i ~/.ssh/pay-wallet-poc.ssh docker-compose.uat.yml kiran@34.122.139.150:/home/kiran/pay-wallet
scp -i ~/.ssh/pay-wallet-poc.ssh bin/wait-for-it.sh kiran@34.122.139.150:/home/kiran/pay-wallet/bin/
scp -i ~/.ssh/pay-wallet-poc.ssh borrower-service/target/borrower-service-0.0.1-SNAPSHOT.jar kiran@34.122.139.150:/home/kiran/pay-wallet/borrower-service/target/
scp -i ~/.ssh/pay-wallet-poc.ssh borrower-service/Dockerfile kiran@34.122.139.150:/home/kiran/pay-wallet/borrower-service/
ssh -i ~/.ssh/pay-wallet-poc.ssh kiran@34.122.139.150 'cd pay-wallet && docker-compose -f docker-compose.uat.yml down'
ssh -i ~/.ssh/pay-wallet-poc.ssh kiran@34.122.139.150 'cd pay-wallet && docker-compose -f docker-compose.uat.yml up --build -d'