## About

This project is a simple RESTful API for managing users. It provides endpoints for creating, updating, deleting, and
retrieving users.

## Used Stack:

- **Development:** Java 17, Spring Boot 3.2.5
- **Build Tool:** Maven
- **Testing:** JUnit 5, AssertJ, Mockito
- **Code Simplification and Logging:** Lombok, SLF4J with Logback
- **Code Quality Tools:** Maven plugins (Checkstyle, PMD)
- **Test Coverage Tools:** JaCoCo Maven Plugin (aiming for 80% coverage)
- **CI/CD:** GitHub Actions
- **Monitoring:** Spring Boot Actuator
- **Mapping:** MapStruct
- **API Documentation:** Swagger, Javadoc
- **Database Migrations:** Liquibase
- **Database:** PostgreSQL

## How to run?

1) Clone repository

```shell
   git clone https://github.com/AndrewPleskanko/ProfITsoft_Course_Task2
```

2) Build project

```shell
   mvn clean install
```

3.1) Run project on Java

```shell
    cd block2
    mvn spring-boot:run
```
3.2) Run project on React

```shell
   cd ui-react
   npm start
```
4) Use VM options to set the database connection details

 ```copy
-DDB_USERNAME=name
-DDB_PASSWORD=pass
-DDB_HOST=localhost:5432
-DDB_NAME=dbName
```

## System design

In this project, we utilize the [Strategy pattern](https://refactoring.guru/design-patterns/strategy) to generate reports in various formats. The ReportStrategy interface is
implemented by different classes, each responsible for generating a report in a specific format like CSV or Excel. The
ReportService uses these strategies, allowing for flexible report format output without altering its own code

## How to test?

1) Open Swagger UI to test the endpoints

```copy
http://localhost:8080/swagger-ui.html
```

2) Use Postman collection to test the endpoints

```copy
ProfITsoft.postman_collection.json
```