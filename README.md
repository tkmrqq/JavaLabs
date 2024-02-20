# Search anime
## Basic REST API service

1) Simple web/REST service Spring to search anime description by his title. Get and Post endpoints"
2) You can send GET request: `http://localhost:8080/anime?title=naruto`
   and POST request:
   `{
    "animeName": "cowboy bebop"
   }`
   

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=tkmrqq_JavaLabs)](https://sonarcloud.io/summary/new_code?id=tkmrqq_JavaLabs)

## General requirements:
1. Git repository with a well-formatted Readme. Usage of gitignore files.
2. SonarCloud with 0 bugs & 0 code smells.
3. Variable, method, class names, and other identifiers should be meaningful.
4. Adhere to Java Code Convention when writing code.
5. Follow a layered architecture approach for the application.
6. Avoid having source code in a single class that handles both data, logic, and external interface. Separate logic, data, and presentation into different types of classes and packages.
7. Do not place the entire application's code within a single method (even if there seems to be "nothing else to write" - additional tasks will be assigned for this).
8. Implementation of the DAO layer is mandatory when designing data access.

## Conditions:
Basic REST service:
- Create and locally run a simple web/REST service using any open example with the Java stack: Spring (Spring Boot)/maven/gradle/Jersey/ Spring MVC.
- Add a GET endpoint that accepts input parameters as queryParams in the URL according to the variant, and returns any hard-coded result in JSON format according to the variant.

## Dependendencies
- Spring Boot
- Spring Web
- RestTemplate
- JSON.org
- PostgreSQL

## Installation:
- Clone the repository: `git clone https://github.com/tkmrqq/JavaLabs.git`
- Open the project in your preferred Java IDE.
- Build the project using Maven or Gradle.
- Set up the database connection in the application.properties file (Use PostgreSQL).
- Run the application.
 
 ## API
[API SRC](https://docs.api.jikan.moe/)

# Spring HELP:
### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.2/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#io.validation)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)

<img src="https://iis.bsuir.by/api/v1/employees/photo/532500">
