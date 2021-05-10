# safetynetalerts
Openclassrooms project number 5

<!-- ABOUT THE PROJECT -->
## About The Project

Back-end application for the safetynet alerts system. This is a Spring Boot application for project number 5 of [Openclassrooms](https://openclassrooms.com/) java back-end formation.

The application goals:
* The application deserializes and serializes data from a json file (original JSON file [here](https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json))
* The application replies to [GET requests](https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DAJava_P5/URLs.pdf) in JSON format.
* The application receives [POST/UPDATE/DELETE requests](https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DAJava_P5/Endpoints.pdf) and serializes data accordingly.

### Built With

* [Java 11](https://adoptopenjdk.net/)
* [Maven 3.8.1](https://maven.apache.org/download.cgi#downloading-apache-maven-3-8-1)

<!-- GETTING STARTED -->
## Getting Started

This is how to set up the project locally.
To get a local copy up and running follow these simple example steps:

### Prerequisites

Check that you have : 
* Java 11 installed
  ```sh
  java -version
  ```
* Maven 3.8.1 installed
  ```sh
  mvn -v
  ```

### Installation

1. Choose a directory
   ```sh
   cd /path/to/directory/project
   ```
2. Clone the repo
   ```sh
   git clone https://github.com/jerome13250/safetynetalerts.git
   ```
3. Select the safetynetalerts directory
   ```sh
   cd safetynetalerts
   ```
4. Package the application (fat jar file)
   ```sh
   mvnw package
   ```
5. Execute the jar file
   ```JS
   java -jar ./target/SafetyNetAlerts-0.0.1-SNAPSHOT.jar
   ```
   NOTE: respect the execution of jar from safetynetalerts folder as done above as the application loads json file in safetynetalerts/json/ folder.



<!-- USAGE EXAMPLES -->
## Usage

The application usage is documented with Swagger, when the jar is running type in browser : http://localhost:8080/swagger-ui/#/

Otherwise in a browser you can directly try the following GET requests:

http://localhost:8080/firestation?stationNumber=3

http://localhost:8080/childAlert?address=1509%20Culver%20St

http://localhost:8080/phoneAlert?firestation=4

http://localhost:8080/fire?address=1509%20Culver%20St

http://localhost:8080/flood/stations?stations=1,2,3,4

http://localhost:8080/personInfo?firstName=Lily&lastName=Cooper

http://localhost:8080/communityEmail?city=Culver

The following Actuators are available:

[INFO](http://localhost:8080/actuator/info)

[HEALTH](http://localhost:8080/actuator/health)

[METRICS](http://localhost:8080/actuator/metrics)

[HTTPTRACE](http://localhost:8080/actuator/httptrace)
