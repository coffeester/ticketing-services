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
