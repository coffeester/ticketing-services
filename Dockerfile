FROM docker.io/centos:latest
RUN	echo "root:1234qwer" | chpasswd

RUN	yum install -y java-1.8.0-openjdk

ADD     build/libs/ticketing-services-0.0.1-SNAPSHOT.jar ticketing-services-0.0.1-SNAPSHOT.jar

ADD	config/application.yml	application.yml	

CMD     ["java -jar ticketing-services-0.0.1-SNAPSHOT.jar"]

ENTRYPOINT ["java","-Dspring.config.location=.","-jar","/ticketing-services-0.0.1-SNAPSHOT.jar"]
