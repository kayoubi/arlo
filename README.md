Steps to install and start

```
git clone git@github.com:kayoubi/arlo.git

cd arlo

mkdir mongo-data

docker run -d --name=arlo-spring-mongo -v `pwd`/mongo-data:/data/db -p 127.0.0.1:27017:27017 -p 127.0.0.1:28017:28017 mongo -smallfiles --rest --httpinterface

mvn clean install

java -jar ./target/arlo-0.1.jar
```