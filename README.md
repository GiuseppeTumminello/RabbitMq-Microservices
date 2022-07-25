# Salary Calculator Microservices Project

The project is composed by 14 microservices which calculates different taxes applied to the gross salary amount.
Below, you can find the explanation of each microservice and how to use them.

### Total Zus Microservice

The microservice calculates the total zus amount from the monthly gross salary.
The endpoints are available in the following endpoints using the HTTP POST method:

* http://localhost:8080/total-zus/calculation/8000
* http://localhost:8098/total-zus/calculation/8000

The response will look like as following:

{
"value": "391.99",
"description": "Tax"
}

The actuator will be accessible via the following links:

* http://localhost:8080/total-zus/actuator
* http://localhost:8098/total-zus/actuator


The first endpoint is accessible via Spring api Gateway and the second one through the server port.

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=total-zus
* http://localhost:8098/total-zus/v3/api-docs

The first endpoints is accessible via Spring api Gateway and the second ones through the server port.

### Tax Microservice

The microservice calculates the Tax amount from the monthly gross salary.
The endpoints are available in the following endpoints using the HTTP POST method:

* http://localhost:8080/tax/calculation/8000
* http://localhost:8098/tax/calculation/8000

The response will look like as following:

{
"value": "391.99",
"description": "Tax"
}

The actuator will be accessible via the following links:

* http://localhost:8080/tax/actuator
* http://localhost:8090/tax/actuator


The first endpoint is accessible via Spring api Gateway and the second one through the server port.

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=tax
* http://localhost:8090/tax/v3/api-docs

The first endpoints is accessible via Spring api Gateway and the second ones through the server port.

### Rabbit MQ

The project is built asynchronously using Rabbit MQ, click the following link to access to the dashboard:

* http://192.168.99.105:15672/

Replace "192.168.99.105" with your docker ip machine.

### Spring Admin Microservice

The spring admin microservices displays all the services up and show them in a dashboard.
In order to access to the dashboard, go to the following links:

* http://localhost:8080/monitoring/microservices/applications



The Spring admin Microservice provides also an actuator where you can find all the information,
The actuator is accessible via the following link:

* http://localhost:8080/monitoring/actuator

The endpoints are only accessible via api gateway.



### Sickness Zus Microservice

The microservice calculates the sickness zus amount from the monthly gross salary.
The endpoints are available in the following endpoints using the HTTP POST method:

* http://localhost:8080/sickness-zus/calculation/8000
* http://localhost:8083/sickness-zus/calculation/8000

The response will look like as following:

{
"value": "391.99",
"description": "SicknessZus"
}

The actuator will be accessible via the following links:

* http://localhost:8080/sickness-zus/actuator
* http://localhost:8083/sickness-zus/actuator


The first endpoint is accessible via Spring api Gateway and the second one through the server port.

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=sickness-zus
* http://localhost:8083/sickness-zus/v3/api-docs

The first endpoints is accessible via Spring api Gateway and the second ones through the server port.

### Pension Zus Microservice

The microservice calculates the Pension Zus amount from the monthly gross salary.
The endpoints are available in the following endpoints using the HTTP POST method:

* http://localhost:8080/pension-zus/calculation/8000
* http://localhost:8081/pension-zus/calculation/8000

The response will look like as following:

{
"value": "391.99",
"description": "Pension zus"
}

The actuator will be accessible via the following links:

* http://localhost:8080/pension-zus/actuator
* http://localhost:8081/pension-zus/actuator


The first endpoint is accessible via Spring api Gateway and the second one through the server port.

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=pension-zus
* http://localhost:8081/pension-zus/v3/api-docs

The first endpoints is accessible via Spring api Gateway and the second ones through the server port.

### Monthly Net Microservice

The microservice calculates the Monthly Net amount from the monthly gross salary.
The endpoints are available in the following endpoints using the HTTP POST method:

* http://localhost:8080/monthly-net/calculation/8000
* http://localhost:8092/monthly-net/calculation/8000

The response will look like as following:

{
"value": "391.99",
"description": "Monthly net"
}

The actuator will be accessible via the following links:

* http://localhost:8080/monthly-net/actuator
* http://localhost:8092/monthly-net/actuator


The first endpoint is accessible via Spring api Gateway and the second one through the server port.

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=monthly-net
* http://localhost:8092/monthly-net/v3/api-docs

The first endpoints is accessible via Spring api Gateway and the second ones through the server port.

### Monthly Gross Microservice

The microservice calculates the Monthly Gross amount from the monthly gross salary.
The endpoints are available in the following endpoints using the HTTP POST method:

* http://localhost:8080/monthly-gross/calculation/8000
* http://localhost:8094/monthly-gross/calculation/8000

The response will look like as following:

{
"value": "391.99",
"description": "Monthly gross"
}

The actuator will be accessible via the following links:

* http://localhost:8080/monthly-gross/actuator
* http://localhost:8094/monthly-gross/actuator


The first endpoint is accessible via Spring api Gateway and the second one through the server port.

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=monthly-gross
* http://localhost:8094/monthly-gross/v3/api-docs

The first endpoints is accessible via Spring api Gateway and the second ones through the server port.

### Health Microservice

The microservice calculates the Health amount from the monthly gross salary.
The endpoints are available in the following endpoints using the HTTP POST method:

* http://localhost:8080/health/calculation/8000
* http://localhost:8088/health/calculation/8000

The response will look like as following:

{
"value": "391.99",
"description": "Health"
}

The actuator will be accessible via the following links:

* http://localhost:8080/health/actuator
* http://localhost:8088/health/actuator


The first endpoint is accessible via Spring api Gateway and the second one through the server port.

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=health
* http://localhost:8088/health/v3/api-docs

The first endpoints is accessible via Spring api Gateway and the second ones through the server port.

### Discovery Server

The Discovery server  allows clients applications to find services through a router or a load balancer,
It provides also a dashboard where there are displayed all the services registered, and it is accessible from the following link:

* http://localhost:8080/eureka/microservices

The discovery server is only accessible through api gateway

### Disability Zus Microservice

The microservice calculates the Disability Zus amount from the monthly gross salary.
The endpoints are available in the following endpoints using the HTTP POST method:

* http://localhost:8080/disability-zus/calculation/8000
* http://localhost:8084/disability-zus/calculation/8000

The response will look like as following:

{
"value": "391.99",
"description": "Disability zus"
}

The actuator will be accessible via the following links:

* http://localhost:8080/disability-zus/actuator
* http://localhost:8084/disability-zus/actuator


The first endpoint is accessible via Spring api Gateway and the second one through the server port.

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=disability-zus
* http://localhost:8084/disability-zus/v3/api-docs

The first endpoints is accessible via Spring api Gateway and the second ones through the server port.

### Api gateway

API gateway is an API management tool that sits between a client and a collection of backend services,
The api gateway is exposed via the following endpoint:

* http://localhost:8080/

The Spring admin Microservice provides also an actuator where you can find all the information,
The actuator is accessible via the following link:

* http://localhost:8080/actuator

### Annual Net Microservice

The microservice calculates the Annual net amount from the monthly gross salary.
The endpoints are available in the following endpoints using the HTTP POST method:

* http://localhost:8080/annual-net/calculation/8000
* http://localhost:8093/annual-net/calculation/8000

The response will look like as following:

{
"value": "391.99",
"description": "Annual net"
}

The actuator will be accessible via the following links:

* http://localhost:8080/annual-net/actuator
* http://localhost:8093/annual-net/actuator


The first endpoint is accessible via Spring api Gateway and the second one through the server port.

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=annual-net
* http://localhost:8093/annual-net/v3/api-docs

The first endpoints is accessible via Spring api Gateway and the second ones through the server port.

### Annual Gross Microservice

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

### Salary Calculator Microservice

The rest API has been created to calculate the net amount in PLN and display all the taxation applied to the gross amount.

The application can perform 2 GET and 1 POST HTTP methods. The GET methods show the department name and the job titles list available, and they are accessible from the following links:

* http://localhost:8080/salary-calculations/departments
* http://localhost:8080/salary-calculations/jobs/{departmentName}

  Replace {departmentName} with the job department name.

The POST HTTP method is used to calculate the net salary and accepts optional query parameters. Go to the following endpoint to calculate your net salary or participate in the statistics:

* http://localhost:8080/salary-calculations/calculations/{grossSalaryMonthly}
* http://localhost:8080/salary-calculations/calculations/{grossSalaryMonthly}?departmentName={departmentName}&jobTitleId={id}

or without api gateway:

* http://localhost:8096/salary-calculations/calculations/{grossSalaryMonthly}
* http://localhost:8096/salary-calculations/calculations/{grossSalaryMonthly}?departmentName={departmentName}&jobTitleId={id}

If the user wants to participate in the statistic, add to the url the department name and the job title id. The endpoint is accessible via http://localhost:8080/salary-calculations/calculations/{grossSalaryMonthly}?departmentName={departmentName}&jobTitleId={id}. Replace {grossSalaryAmount} with your gross salary, {departmentName} with one of the department names listed below, and the {jobTitleId} with one of the job titles id listed below.

An example of the endpoint:

* http://localhost:8080/salary-calculations/calculations/6000?departmentName=finance&jobTitleId=9

Department name:

* It
* Finance
* Engineer
* Restaurant
* Airline

Job title id:

* It:
    * 1 - DevOps Engineer
    * 2 - Software Developer
    * 3 - Software Engineer
    * 4 - Cloud System Engineer
    * 5 - Cloud Architect
    * 6 - IT Analyst
    * 7 - Network Engineer
    * 8 - IT Support Specialist
    * 9 - Database Administrator
    * 10 - System Architect
    * 11 - Web Administrator

* Finance:

    * 1 - Fund Accountant
    * 2 - Depositary
    * 3 - Accountant
    * 4 - Tax Analyst
    * 5 - Auditor
    * 6 - Risk Analyst
    * 7 - Business Analyst
    * 8 - Billing Administrator
    * 9 - Financial Controller

* Engineer:
    * 1 - Mechanical Engineer
    * 2 - Civil Engineer
    * 3 - Project Engineer
    * 4 - Sales Engineer
    * 5 - R&D Engineer
    * 6 - Thermal Engineer

* Restaurant
    * 1 - Executive Chef
    * 2 - Assistant Manager
    * 3 - General Manager
    * 4 - Sous Chef
    * 5 - Pastry Chef
    * 6 - Kitchen Manager
    * 7 - Line Cook
    * 8 - Bartender
    * 9 - Cashier
    * 10 - Dishwasher
    * 11 - Waitress

* Arline
    * 1 - Air Crew
    * 2 - Airline Captain
    * 3 - Airline Pilot
    * 4 - Airport Manager
    * 5 - Analyst
    * 6 - Chief Pilot
    * 7 - Traffic Manager

After providing the parameters, the API will return the following response body:

* Pension Zus amount
* Disability zus amount
* Sickness zus amount
* Total zus amount
* Health amount
* Tax amount
* Yearly gross amount
* Yearly net amount
* Yearly net amount
* Net amount

If the user wants to participate in the statistics will also display the average value.

* Average

The application provide also an actuator which is accessible via the following endpoints:

* http://localhost:8080/salary-calculations/actuator
* http://localhost:8096/salary-calculations/actuator

Swagger it is available via the following endpoints:

* http://localhost:8080/swagger-ui/?urls.primaryName=salary-calculator
* http://localhost:8096/salary-calculations/v3/api-docs

# Setup Salary Calculator Microservices Project


* Required:
    * Docker


* To create a container in Docker, follow the below instructions:

    * Go to the folder: Spring-SalaryCalculator-Microservices
    * Create a jar file for each microservice by running "gradle build" or "gradle bootJar"
    * Update the .env file with your docker machine ip, the ip can be retrieved by running "docker-machine ip" 
    * execute: docker-compose -f docker-compose.yml up



# Related Git Repositories

* https://github.com/GiuseppeTumminello/Microservice-AnnualGross.git
* https://github.com/GiuseppeTumminello/Microservice-AnnualNet.git
* https://github.com/GiuseppeTumminello/Microservice-api-gateway.git
* https://github.com/GiuseppeTumminello/Microservice-disability-zus.git
* https://github.com/GiuseppeTumminello/Microservice-discovery-server.git
* https://github.com/GiuseppeTumminello/Microservice-health.git
* https://github.com/GiuseppeTumminello/Microservice-monthlyGross.git
* https://github.com/GiuseppeTumminello/Microservices-MonthlyNet
* https://github.com/GiuseppeTumminello/Microservice-pension-zus.git
* https://github.com/GiuseppeTumminello/Microservices-sickness-zus.git
* https://github.com/GiuseppeTumminello/Microservice-SpringAdmin.git
* https://github.com/GiuseppeTumminello/Microservice-tax
* https://github.com/GiuseppeTumminello/Microservice-total-zus.git
* https://github.com/GiuseppeTumminello/Microservice-total-zus.git
* https://github.com/GiuseppeTumminello/Microservice-SalaryCalculator.git





    




