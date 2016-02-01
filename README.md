## Ticketing-Services

**Building, starting & testing ticketing service**

```shell

git clone https://github.com/coffeester/ticketing-services.git

cd ticketing-services

./gradlew clean bootRepackage

```

Start the application

```java
java -Dspring.config.location="config/" -jar build/libs/ticketing-services-0.0.1-SNAPSHOT.jar
```

Once the server is started, click link and open in the browser:
[http://localhost:8080/swagger/index.html](http://localhost:8080/swagger/index.html)


Alternatively, you can use terminal to execute curl commands:

#### Get Available seats
/api/v1/ticketing/seats

request param: level (1 to 4)

```sh
curl "http://localhost:8080/api/v1/ticketing/seats?level=1"
```


#### Hold number of seats
/api/v1/ticketing/hold

```sh
curl "http://localhost:8080/api/v1/ticketing/hold?numberOfSeats=1&minLevel=1&maxLevel=3&customerEmail=johndoe@example.com"

```

#### Reserve held seats
/api/v1/ticketing/reserve

```sh

curl "http://localhost:8080/api/v1/ticketing/reserve?seatHoldId=1&customerEmail=johndoe@example.com"

```
**Execute Unit tests**

```sh
./gradlew clean build

#OR for more verbose output

./gradlew clean test -i
```
###Assumptions

1. Tickets will be held starting from lowest level to highest.
2. Level will default to min=1 and max=4; if no level specified.
3. Some data has been seeded into seat table to run above curl commands. You can use the same request parameters in swagger-ui.
4. To squeeze the dataset the seeded data is only for level 1 & 2 with 6 seats each. [seeded data](https://github.com/coffeester/ticketing-services/blob/master/src/main/resources/import.sql)
5. Default customerEmail = johndoe@example.com
6. App-configuration file application.yml is externalized under ticketing-services/config and is not available in the fatJar. Please do not forget to use (same if you are importing in idea/eclipse) -Dspring.config.location="config/"  
