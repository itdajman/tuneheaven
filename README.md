# TuneHeaven Analytics

## Overview
TuneHeaven Analytics is a project that provides analytics and reporting capabilities for song data. It is generating reports on song trends and analyze changes in song ratings over time.

## Technologies Used
- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database
- JUnit and Mockito (for testing)
- CSV export for reporting

## Getting Started

### Clone the repository
To get started, clone this repository to your local machine

### Run the application
Once the build is successful, you can run the application using:

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.


## Running Tests
To run the test suite, execute:

```bash
mvn test
```

## API Documentation
After running the application, you can access the Swagger UI for the API documentation at `http://localhost:8080/swagger-ui.html`.

## Docker
`docker build -t your-image-name .` \
`docker run -p 8080:8080 your-image-name`

