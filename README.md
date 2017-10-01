# Arlo Login

There are few ways to try the application 

## Get your hands dirty 

This assumes you have `git` & `java` & `docker` installed locally **_and_** some time; although you can get away with not having all these (see Variations below) 

- go to your working directory 
- clone the repository 
```bash
git clone git@github.com:kayoubi/arlo.git
cd arlo
```
- create directory for mongo data (so your data are not lost when restarting the container)  
- start mongo docker container 
```bash
mkdir mongo-data
docker run -d --name=arlo-spring-mongo -v `pwd`/mongo-data:/data/db -p 127.0.0.1:27017:27017 -p 127.0.0.1:28017:28017 mongo -smallfiles --rest --httpinterface
```
- build and start the application 

```bash
./mvnw clean install
java -jar ./target/arlo-0.1.jar
```
- go to `http://localhost:8787/arlo/login`
- use `arlo/arlo-admin` to login (or something else to get a failed result) 

## Variations
- if you don't have `git` or you don't want to build yourself, just download the jar file from [here](https://www.dropbox.com/sh/43kcpbtm4930tz3/AACdeyCT_tP-L59iN3ea920ea?dl=0) and run the command above
- if you don't have `docker` or just want to connect to your running mongodb, pass this param to the command line (default is `mongodb://127.0.0.1:27017/arlo`)
```bash
--spring.data.mongodb.uri=mongodb://user:password@host:port/db-name
```  

## You kidding me! I'm too busy 
Fair enough, AWS to the rescue :) just browse the code here, I deployed the application/DB to AWS EC2
- provisioned EC2 instance with Docker using `docker-machine`
- started mongodb container as above 
- docker-ized the spring app [here](https://github.com/kayoubi/arlo/blob/master/Dockerfile)
- published a docker image to [docker hub](https://hub.docker.com/r/kayoubi/arlo/)
- started arlo container `docker run -d -p 8787:8787 --link arlo-spring-mongo:arlo-mongo kayoubi/arlo`
- link to test [here](http://ec2-54-245-57-68.us-west-2.compute.amazonaws.com:8787/arlo/login)


# REST API
So beside the fancy default spring security login page, there are few REST end points to try from your REST client

**Login (using JSON load)**
---
By default spring-security supports form submit `application/x-www-form-urlencoded` but I added support for JSON payload as well (see [JsonUsernamePasswordAuthenticationFilter](https://github.com/kayoubi/arlo/blob/master/src/main/java/com/arlo/user/security/JsonUsernamePasswordAuthenticationFilter.java))
- **URL**

    `/arlo/login`
- **Method**

    `POST`
- **Data Params**
```json
    {
      "username": "arlo",
      "password": "arlo-admin"
    }
```
* **Success Login:**

  * **Code:** 200 OK  
  * **Content:** `{ "success" : true}`
 
* **Failed Login:**

  * **Code:** 401 UNAUTHORIZED 
  * **Content:** `{ "success" : false}`
    
    
**Login (not using spring-security)**
---
spring-security does a lot of work for us, this is another entry point for login where we handle the request ourselves (if the exercise was not meant to use spring-security)
- **URL**

    `/arlo/login2`
- **Method**

    `POST`
- **Data Params**
    ```json
    {
      "username": "arlo",
      "password": "arlo-admin"
    }
    ```
- **Success Login:**

  - **Code:** 200 OK
  - **Content:** `{ "success" : true}`
 
- **Failed Login:**

  - **Code:** 401 UNAUTHORIZED 
  - **Content:** `{ "success" : false}`
    
    
**Create new Account**
---
Signup kind of request, so we are not stuck with the default account
- **URL**

    `/arlo/accounts`
- **Method**

    `POST`
- **Data Params**

  - *username* is mandatory and unique
  - *password* is mandatory

    ```json
    {
      "username": "superman",
      "password": "kryptonite",
      "firstName": "Super",
      "lastName": "Hero"
    }
    ```
- **Success Response:**

  - **Code:** 201 CREATED
  - **Header** `Location: http://localhost:8787/arlo/accounts/{id}`
 
- **Failed Response:**

  - **Code:** 400 BAD_REQUEST  
  
  
  **List all accounts**
  ---
  No you can't see the passwords, and they are encrypted anyway
  
  - **URL**
  
      `/arlo/accounts`
  - **Method**
  
      `GET`
  
  * **Success Result:**
  
    * **Code:** 200 OK
    * **Content:** 
      ```json
      [
        {
          "id": "59cefdff417f4d9ec3458062",
          "username": "kayoubi",
          "password": "******",
          "firstName": "Khaled",
          "lastName": "Ayoubi"
        },
        {
          ....
        }
      ]
      ``` 
      
  **Get one account**
  ---
  This can be done by either using the document id or the username
  - **URL**
  
      `/arlo/accounts/{id}`
  - **Method**
  
      `GET`
  
  * **Success Result:**
  
    * **Code:** 200 OK
    * **Content:** 
      ```json      
      {
        "id": "59cefdff417f4d9ec3458062",
        "username": "kayoubi",
        "password": "******",
        "firstName": "Khaled",
        "lastName": "Ayoubi"
      }
      ```    
      
  * **Failed Result:**
    * **Code:** 404 NOT_FOUND