# Spring Admin Microservice

The spring admin microservices displays all the services up and show them in a dashboard.
In order to access to the dashboard, go to the following links:

* http://localhost:8080/monitoring/microservices/applications



The Spring admin Microservice provides also an actuator where you can find all the information,
The actuator is accessible via the following link:

* http://localhost:8080/monitoring/actuator

The endpoints are only accessible via api gateway.

# Setup

The project is strictly connected with its parent project "Spring-SalaryCalculator-Microservices",
Please make sure to clone the parent repository.

* Required:
    * Docker


* To create a container in Docker, follow the below instructions:

    * Go to the folder: Spring-SalaryCalculator-Microservices
    * Create a jar file running "gradle build" or "gradle bootJar"
    * execute: docker-compose -f docker-compose.yml up



