# Assingment Application

This is a Spring Boot application that manages customers and their transactions.

## Features

- Customer management: Create and retrieve customer information.
- Transaction management: Perform top-up, purchase, and refund transactions for customers.

## Technologies Used

- Java
- Spring Boot
- Gradle
- JPA/Hibernate
- PostgreSQL DB

## Getting Started

### Prerequisites

- Java 17
- Gradle

## Local Database Setup

This application uses a PostgreSQL database for data storage. A Docker Compose file is provided in the `docker` directory for setting up a local PostgreSQL database for testing purposes.

Before running the application, start the PostgreSQL database using Docker Compose with the following steps:

1. Navigate to the `docker` directory: `cd docker`
2. Start the PostgreSQL database: `docker-compose -f postgresql-compose.yml up -d`

This will start a PostgreSQL database in a Docker container. The application is configured to connect to this database when running locally.

Please ensure that Docker is installed and running on your machine before executing these commands.

### Running the Application

1. Navigate to the project directory: `cd assingment`
2. Build the project: `gradle build`
3. Run the application: `gradle bootRun`

The application will start running at `http://localhost:8080`.

## API Endpoints

- `POST /api/v1/customers`: Create a new customer.
- `GET /api/v1/customers`: Retrieve all customers.
- `GET /api/v1/customers/{customerId}`: Retrieve a customer by ID.
- `POST /api/v1/transactions/{customerId}/top-up`: Perform a top-up transaction for a customer.
- `POST /api/v1/transactions/{customerId}/purchase`: Perform a purchase transaction for a customer.
- `POST /api/v1/transactions/{customerId}/refund`: Perform a refund transaction for a customer.

## Authentication and Authorization

This application uses Basic Authentication with in-memory user details for securing the endpoints. Currently, there are two default users:

- User:
    - Username: user
    - Password: 4s3r
    - Roles: USER
- Admin:
    - Username: admin
    - Password: @dm1n
    - Roles: USER, ADMIN

- User: Has access to customer endpoints.
- Admin: Has access to both customer and transaction endpoints.

Here are the access controls for different endpoints:

- Swagger UI (`/swagger-ui.html`): No authentication is required to access the Swagger UI. However, to test the endpoints using Swagger UI, you need to authorize using the required credentials.
- Customer endpoints (`/api/v1/customers`): You need to be authenticated as a user or admin to access these endpoints.
- Transaction endpoints (`/api/v1/transactions`): You need to be authenticated as an admin to access these endpoints.

When you start the application and navigate to the index page, you will be automatically redirected to the Swagger UI.

Please note that you need to include the Basic Authentication header in your requests when accessing the secured endpoints. The header is in the format `Authorization: Basic <credentials>`, where `<credentials>` is the Base64 encoding of your username and password joined by a colon.

## Testing

Run the tests with the following command: `gradle test`