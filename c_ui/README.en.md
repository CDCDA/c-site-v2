# C-Site-V2 Personal Website System

## Project Overview

C-Site-V2 is a complete personal website system built with a frontend-backend separation architecture. The backend is built on Spring Boot and provides a comprehensive RESTful API; the frontend is developed using Vue 3 + Vite, offering a modern user interface. The system integrates multiple content management features including blog, album, games, movies, and supports advanced features such as user authentication, WebSocket real-time communication, and WeChat Official Account integration.

## Technology Stack

### Backend Technologies

- **Framework**: Spring Boot 3.x
- **Database**: MyBatis Plus + MySQL
- **Cache**: Redis
- **Authentication**: JWT (JSON Web Token)
- **Documentation**: Swagger / OpenAPI
- **WebSocket**: Spring WebSocket
- **Task Scheduling**: Spring Scheduling
- **Others**: Lombok, Hutool, FastJSON

### Frontend Technologies

- **Framework**: Vue 3 + Vite
- **UI Components**: Element Plus
- **State Management**: Pinia
- **Routing**: Vue Router
- **HTTP Client**: Axios
- **Build Tool**: Vite

## Features

### Content Management

- **Blog System**: Supports blog categories, tag management, comments, and statistics by time, category, and tag
- **Album Management**: Image album management with image association
- **Game Management**: Game information and category statistics
- **Media Management**: Movie and TV resource management with category statistics
- **Essays Management**: Free-form article management
- **To-Do List**: Todo list management
- **Update Log**: Version update records

### System Features

- **User Management**: User registration, login, and permission management
- **Dictionary Data**: Management of system dictionary types and data
- **File Upload**: Supports file upload functionality
- **Messaging System**: In-site message notifications
- **Comment System**: Tree-structured comment system

### Advanced Features

- **JWT Authentication**: Stateless user authentication with guest login support
- **WebSocket**: Real-time message push and disk monitoring notifications
- **WeChat Official Account**: Integration with WeChat Official Platform, supports template messages
- **Node-RED Integration**: IoT device connectivity
- **Internationalization Support**: Multi-language frontend interface

## Project Structure

```
c-site-v2/
├── c_server/                    # Spring Boot Backend
│   ├── src/main/java/com/pw/
│   │   ├── PServerV3Application.java    # Application Entry
│   │   ├── common/                      # Common Components
│   │   │   ├── aspect/                  # AOP Aspects
│   │   │   ├── constants/               # Constant Definitions
│   │   │   ├── controller/              # Base Controllers
│   │   │   ├── entity/                  # Base Entities
│   │   │   ├── exception/               # Exception Handling
│   │   │   ├── filter/                  # Filters
│   │   │   ├── handler/                 # Handlers
│   │   │   ├── mybatisPlus/             # MyBatis Plus Configuration
│   │   │   ├── token/                  # Authentication Annotations
│   │   │   └── utils/                   # Utility Classes
│   │   ├── config/                     # Configuration Classes
│   │   ├── controller/                 # Controllers
│   │   ├── domain/                     # Entity Classes
│   │   ├── dto/                        # Data Transfer Objects
│   │   ├── mapper/                     # Data Access Layer
│   │   ├── monitor/                    # Monitoring System
│   │   ├── service/                    # Business Services
│   │   └── vo/                         # View Objects
│   └── src/main/resources/
│       ├── application.yml             # Application Configuration
│       └── mapper/                     # MyBatis XML Mappings
│
└── c_ui/                         # Vue 3 Frontend
    ├── src/                      # Source Code
    ├── public/                   # Static Resources
    ├── dist-pagination/          # Built Components
    ├── lib/                      # Third-party Libraries
    └── i18nScripts/             # Internationalization Scripts
```

## Environment Requirements

### Backend

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

### Frontend

- Node.js 16+
- npm 8+ or yarn 1.22+

## Quick Start

### Backend Deployment

1. **Configure Database and Redis**

Update the database and Redis connection settings in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
  redis:
    host: localhost
    port: 6379
```

2. **Build the Project**

```bash
cd c_server
./mvnw clean package -DskipTests
```

3. **Run the Project**

```bash
java -jar target/p-server-v3.jar
```

Or run directly with Maven:

```bash
./mvnw spring-boot:run
```

### Frontend Deployment

1. **Install Dependencies**

```bash
cd c_ui
npm install
# or
yarn install
```

2. **Development Mode**

```bash
npm run dev
```

3. **Build Production Version**

```bash
npm run build
```

## API Documentation

After starting the project, access the Swagger API documentation at:

```
http://localhost:8080/swagger-ui/index.html
```

## Main Module Overview

| Module         | Path                     | Description                             |
| -------------- | ------------------------ | --------------------------------------- |
| Blog           | /blogs                   | Blog CRUD, categories, tags, statistics |
| Album          | /albums                  | Album and image management              |
| Essays         | /essays                  | Free-form article management            |
| Tags           | /blog-tags               | Blog tag management                     |
| Categories     | /blog-types              | Blog category management                |
| Comments       | /comments                | Comment management with tree structure  |
| Authentication | /auth                    | User login, registration, CAPTCHA       |
| Users          | /users                   | User management                         |
| Files          | /files                   | File upload                             |
| Messages       | /messages                | In-site messaging                       |
| To-Do          | /todos                   | Todo list management                    |
| Dictionaries   | /dict-datas, /dict-types | Dictionary data management              |

## License

This project is intended solely for learning and communication purposes.
