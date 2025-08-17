# SecureBank - Banking Application Backend

A robust banking application backend built with Java 21 and Spring Boot 3.2.4 that provides comprehensive banking services through a RESTful API.

## Features

- Customer Management
- Account Management (Savings, Checking, Business)
- Transaction Processing (Deposits, Withdrawals, Transfers)
- Authentication and Authorization
- Real-time Transaction Monitoring
- Integrated Chatbot Support

## Technology Stack

- Java 21
- Spring Boot 3.2.4
- Spring Security
- Spring Data JPA
- OpenAPI (Swagger) for API Documentation
- Maven for Dependency Management

## Requirements

- Java 21 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

## Getting Started

1. **Clone the repository**

2. **Configure the database**
   - Create a MySQL database
   - Update `application.properties` with your database credentials

3. **Build the application:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   
   Or using the JAR file:
   ```bash
   java -jar target/banking-application-0.0.1-SNAPSHOT.jar
   ```

5. **Access the application:**
   - API Documentation: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/actuator/health

## API Endpoints

### Authentication
- POST `/api/auth/login` - User login
- POST `/api/auth/register` - User registration

### Customers
- GET `/api/customers` - List all customers
- GET `/api/customers/{id}` - Get customer details
- POST `/api/customers` - Create new customer
- PUT `/api/customers/{id}` - Update customer
- DELETE `/api/customers/{id}` - Delete customer

### Accounts
- GET `/api/accounts` - List all accounts
- GET `/api/accounts/{id}` - Get account details
- POST `/api/accounts` - Create new account
- PUT `/api/accounts/{id}` - Update account
- DELETE `/api/accounts/{id}` - Delete account

### Transactions
- GET `/api/transactions` - List all transactions
- GET `/api/transactions/{id}` - Get transaction details
- POST `/api/transactions/deposit` - Make a deposit
- POST `/api/transactions/withdraw` - Make a withdrawal
- POST `/api/transactions/transfer` - Make a transfer

## Project Structure

```
src/
├── main/
│   ├── java/com/application/banking/
│   │   ├── BankingApplication.java
│   │   ├── config/
│   │   │   ├── OpenApiConfig.java
│   │   │   ├── SecurityConfig.java
│   │   │   └── SwaggerConfig.java
│   │   ├── controller/
│   │   │   ├── AccountController.java
│   │   │   ├── AuthController.java
│   │   │   ├── CustomerController.java
│   │   │   └── TransactionController.java
│   │   ├── dto/
│   │   │   ├── AccountDTO.java
│   │   │   ├── CustomerCreateRequest.java
│   │   │   └── TransactionDTO.java
│   │   ├── model/
│   │   │   ├── Account.java
│   │   │   ├── Customer.java
│   │   │   └── Transaction.java
│   │   ├── repository/
│   │   │   ├── AccountRepository.java
│   │   │   ├── CustomerRepository.java
│   │   │   └── TransactionRepository.java
│   │   └── service/
│   │       ├── AccountService.java
│   │       ├── CustomerService.java
│   │       └── TransactionService.java
│   └── resources/
│       └── application.properties
```

## Security

The application implements comprehensive security measures:
- JWT-based authentication
- Role-based access control
- Password encryption
- Secure session management
- Input validation and sanitization

## Error Handling

The application includes a global error handling mechanism that provides:
- Consistent error responses
- Detailed error messages
- Appropriate HTTP status codes
- Transaction rollback on failures

## Monitoring and Logging

- Actuator endpoints for monitoring
- Structured logging with SLF4J
- Transaction audit logging
- Performance metrics

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 