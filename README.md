# SecureBank - Full Stack Banking Application

A modern, secure banking application built with Spring Boot 3.2.4 backend and vanilla JavaScript frontend. This application provides comprehensive banking services including customer management, account operations, and transaction processing.

## 🌟 Features

### Customer Management
- Customer registration and profile management
- KYC document handling
- Customer search and filtering

### Account Operations
- Multiple account types (Savings, Checking, Business)
- Account creation and management
- Balance tracking
- Account status management

### Transaction Processing
- Deposits and withdrawals
- Inter-account transfers
- Transaction history
- Real-time transaction updates

### Security
- JWT-based authentication
- Role-based access control (Admin, Customer, Teller)
- Secure password handling
- Input validation and sanitization

## 🛠️ Technology Stack

### Backend
- Java 21
- Spring Boot 3.2.4
- Spring Security
- Spring Data JPA
- MySQL Database
- Maven
- OpenAPI (Swagger)

### Frontend
- HTML5
- CSS3 (Modern Flexbox/Grid)
- Vanilla JavaScript
- Font Awesome Icons
- Responsive Design

## 📋 Prerequisites

1. Backend:
   - Java Development Kit (JDK) 21
   - Maven 3.6+
   - MySQL 8.0+

2. Frontend:
   - Modern web browser
   - Web server (optional, can use Spring Boot's static serving)

## 🚀 Getting Started

### Backend Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/banking-application.git
   cd banking-application/Bank_application
   ```

2. Configure MySQL:
   - Create a new database named 'bankdb'
   - Update application.properties with your database credentials

3. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Backend will start at: http://localhost:8080

### Frontend Setup

1. Navigate to frontend directory:
   ```bash
   cd banking-frontend
   ```

2. Configure API endpoint:
   - Open js/api.js
   - Update baseURL if your backend runs on a different port

3. Serve frontend:
   ```bash
   # Using Python
   python -m http.server 3000
   
   # OR using Node.js
   npx http-server -p 3000
   
   # OR copy files to Spring Boot's static directory
   cp -r * ../Bank_application/src/main/resources/static/
   ```

4. Access the application at: http://localhost:3000

## 👥 Default Users

The application comes with pre-configured demo accounts:

| Role     | Username           | Password | Access Level                    |
|----------|-------------------|----------|--------------------------------|
| Admin    | admin@bank.com    | admin    | Full system access             |
| Customer | customer@bank.com | password | Personal banking features      |
| Teller   | teller@bank.com   | teller   | Customer service operations    |

## 📁 Project Structure

```
banking-application/
├── Bank_application/           # Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/application/banking/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── model/
│   │   │   │       ├── repository/
│   │   │   │       └── service/
│   │   │   └── resources/
│   │   │       └── application.properties
│   └── pom.xml
│
└── banking-frontend/          # Frontend
    ├── css/
    │   └── styles.css
    ├── js/
    │   ├── api.js
    │   ├── auth.js
    │   └── main.js
    ├── index.html
    └── dashboard.html
```

## 🔒 Security Considerations

1. Production Deployment:
   - Enable HTTPS
   - Update CORS settings
   - Use environment variables for sensitive data
   - Enable CSRF protection
   - Set secure cookie attributes

2. Database:
   - Use connection pooling
   - Implement proper indexing
   - Regular backups
   - Use prepared statements

3. Authentication:
   - Implement password complexity rules
   - Add rate limiting
   - Set up account lockout
   - Enable 2FA (recommended)

## 🛠️ API Documentation

Once the backend is running, access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Spec: http://localhost:8080/v3/api-docs

## 💻 Development

### Backend Development
```bash
# Run tests
mvn test

# Run with development profile
mvn spring-boot:run -Dspring.profiles.active=dev

# Package application
mvn package
```

### Frontend Development
- Modify styles in css/styles.css
- Update API integration in js/api.js
- Add new features in js/main.js

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- The open-source community for various tools and libraries
- Font Awesome for the icons
