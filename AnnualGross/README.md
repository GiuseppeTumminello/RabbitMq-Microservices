# Annual Gross Microservice

The microservice calculates the Annual gross amount from the monthly gross salary.
The endpoints are available in the following endpoints using the HTTP POST method:

* http://localhost:8080/annual-gross/calculation/8000
* http://localhost:8091/annual-gross/calculation/8000

The response will look like as following:

{
"value": "391.99",
"description": "Annual gross"
}

The actuator will be accessible via the following links:

* http://localhost:8080/annual-gross/actuator
* http://localhost:8091/annual-gross/actuator


The first endpoint is accessible via Spring api Gateway and the second one through the server port.

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=annual-gross
* http://localhost:8091/annual-gross/v3/api-docs

The first endpoints is accessible via Spring api Gateway and the second ones through the server port.

# Setup

The project is strictly connected with its parent project "Spring-SalaryCalculator-Microservices",
Please make sure to clone the parent repository.

* Required:
    * Docker


* To create a container in Docker, follow the below instructions:

    * Go to the folder: Spring-SalaryCalculator-Microservices
    * Create a jar file running "gradle build" or "gradle bootJar"
    * execute: docker-compose -f docker-compose.yml up