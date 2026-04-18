# Project Summary

## Overview of Technologies Used
This project is primarily developed using Java and utilizes the Spring Framework for building enterprise applications. The project also leverages various libraries and tools for different functionalities, including:

- **Frameworks**: 
  - Spring Boot
  - MyBatis
  - RabbitMQ
  - Redis

- **Main Libraries**:
  - Lombok (for reducing boilerplate code)
  - Swagger (for API documentation)
  - JWT (for authentication)

## Purpose of the Project
The project appears to be a web application that serves as a backend service for managing various resources such as blogs, users, comments, and more. It likely provides RESTful APIs for frontend applications to interact with, supporting functionalities like user authentication, data management, and real-time communication via WebSockets.

## Build and Configuration Files
The following files are relevant for the configuration and building of the project:

- **Build Files**:
  - `pom.xml` - Maven Project Object Model file for managing project dependencies and build configurations.
  
- **Configuration Files**:
  - `application.yml` - Main application configuration file.
  - `application-no-rabbitmq.yml` - Alternative configuration file without RabbitMQ settings.

## Source Files Location
All source files can be found in the following directory:

- `/src/main/java/com/pw` - Contains the main application code, including controllers, services, entities, and other components.

## Documentation Files Location
Documentation files are typically found in the following directory:

- `/src/main/resources` - This directory contains configuration files, and while it does not explicitly contain documentation files, it is where the main application configuration is stored. Additional documentation may be generated via Swagger and can typically be accessed through the application's API endpoints.

## Summary of Directory Structure
- **Main Source Files**: `/src/main/java/com/pw`
- **Configuration Files**: `/src/main/resources`
- **Mapper XML Files**: `/src/main/resources/mapper`
- **SQL Files**: `/src/main/resources/sql`
- **Test Files**: `/src/test/java` and `/src/test/java/com/pw` for unit tests.

This structure supports a modular design, allowing for easy maintenance and scalability of the application.