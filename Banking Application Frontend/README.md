# Banking Application Frontend

A modern, responsive web frontend for the Banking API built with HTML, CSS, and JavaScript.

## Features

- **User Authentication** - Login with demo accounts or custom credentials
- **Dashboard Overview** - View key metrics and quick actions
- **Customer Management** - View, search, and manage customer data
- **Account Management** - View and manage bank accounts
- **Transaction Management** - View transaction history and perform banking operations
- **Responsive Design** - Works on desktop, tablet, and mobile devices
- **Real-time API Integration** - Direct integration with Banking REST API

## Demo Accounts

The application comes with pre-configured demo accounts:

- **Admin**: `admin` / `admin` (Full access)
- **Customer**: `customer` / `password` (Customer operations)
- **Teller**: `teller` / `teller` (Teller operations)

## Prerequisites

1. **Banking API Backend** - The Spring Boot banking application must be running on `http://localhost:8080`
2. **Web Server** - Any local web server to serve the static files
3. **Modern Browser** - Chrome, Firefox, Safari, or Edge with ES6+ support

## Quick Start

### Option 1: Apache Tomcat (Recommended)
```bash
# Download Tomcat from https://tomcat.apache.org/
# Extract and copy frontend files to webapps directory
cp -r banking-frontend/ /path/to/tomcat/webapps/banking/

# Start Tomcat
cd /path/to/tomcat/bin
./startup.sh  # Linux/Mac
# or startup.bat on Windows

# Open browser to http://localhost:8080/banking/
```

### Option 2: Spring Boot Embedded Server
```bash
# See setup-tomcat.md for detailed Spring Boot setup
# Creates a dedicated server for frontend
mvn spring-boot:run
# Open browser to http://localhost:3000
```

### Option 3: Development Servers (For Testing Only)
```bash
# Python HTTP Server
python -m http.server 8000

# Node.js HTTP Server
npm install -g http-server
http-server -p 8000

# VS Code Live Server Extension
# Right-click index.html → "Open with Live Server"
```

## File Structure

```
banking-frontend/
├── index.html              # Login page
├── dashboard.html           # Main dashboard
├── css/
│   └── styles.css          # All application styles
├── js/
│   ├── auth.js             # Authentication management
│   ├── api.js              # API integration and utilities
│   └── main.js             # Main application logic
└── README.md               # This file
```

## Configuration

The frontend is configured to connect to the backend at `http://localhost:8080/api`. If your backend is running on a different port or host, update the `baseURL` in:

- `js/auth.js` (line 4)
- `js/api.js` (line 4)

```javascript
// Change this line in both files
this.baseURL = 'http://your-backend-host:port/api';
```

## Browser Compatibility

- **Chrome**: 60+
- **Firefox**: 55+
- **Safari**: 12+
- **Edge**: 79+

## Security Features

- **HTTP Basic Authentication** - Secure API communication
- **Session Management** - Automatic login/logout handling
- **Role-based Access** - Different permissions for different user types
- **Input Validation** - Client-side form validation
- **Error Handling** - Comprehensive error messaging

## API Integration

The frontend communicates with the following API endpoints:

### Customer Operations
- `GET /api/customers` - Get all customers
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

### Account Operations
- `GET /api/accounts` - Get all accounts
- `POST /api/accounts` - Create new account
- `PUT /api/accounts/{id}/activate` - Activate account
- `PUT /api/accounts/{id}/deactivate` - Deactivate account

### Transaction Operations
- `GET /api/transactions` - Get all transactions
- `POST /api/transactions/deposit` - Make deposit
- `POST /api/transactions/withdraw` - Make withdrawal
- `POST /api/transactions/transfer` - Transfer money

## Development

### Adding New Features

1. **New Pages**: Add HTML files and link them in navigation
2. **New Styles**: Add CSS to `css/styles.css`
3. **New API Calls**: Add methods to `js/api.js`
4. **New UI Logic**: Add functions to `js/main.js`

### Code Organization

- **HTML**: Semantic structure with accessibility features
- **CSS**: Mobile-first responsive design with CSS Grid and Flexbox
- **JavaScript**: Modular ES6+ code with async/await patterns

## Troubleshooting

### Common Issues

1. **CORS Errors**: Ensure the backend has CORS enabled for your frontend domain
2. **Authentication Failures**: Check that the backend is running and accessible
3. **API Errors**: Open browser developer tools to see detailed error messages
4. **Loading Issues**: Verify all JavaScript files are loading correctly

### Debug Mode

Open browser developer tools (F12) to see:
- API request/response details
- JavaScript errors
- Network connectivity issues

## Production Deployment

For production deployment:

1. **Configure HTTPS**: Use SSL certificates for secure communication
2. **Update API URLs**: Point to production backend endpoints
3. **Optimize Assets**: Minify CSS and JavaScript files
4. **Enable Compression**: Use gzip compression for better performance
5. **Set Up CDN**: Use a CDN for faster global access

## License

This project is licensed under the MIT License.

## Support

For support or questions about the frontend application, please check:
1. Browser developer console for errors
2. Network tab for API communication issues
3. Backend application logs for server-side errors 