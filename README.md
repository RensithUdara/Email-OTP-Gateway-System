# Email OTP Gateway System

A secure and scalable Email OTP (One-Time Password) Gateway system built with Spring Boot. This system provides user authentication, email verification, and secure OTP management functionalities.

## Features

- 🔐 User Authentication with JWT
- ✉️ Email-based OTP Verification
- 👤 User Registration with Email Verification
- 🔑 Password Reset via OTP
- ⏱️ Configurable OTP Expiry Time
- 🛡️ Rate Limiting for OTP Attempts
- 📧 HTML Email Templates
- 🔒 Secure Password Storage
- 🚫 Protection Against Brute Force Attacks

## Technology Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Security**
- **Spring Data JPA**
- **MySQL Database**
- **JWT (JSON Web Tokens)**
- **JavaMail for Email Service**
- **Maven**
- **Lombok**

## Prerequisites

- JDK 17 or later
- Maven 3.6+
- MySQL 8.0+
- SMTP Server access (Gmail or other email service)

## Setup and Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/YourUsername/Email-OTP-Gateway.git
   cd Email-OTP-Gateway
   ```

2. **Configure Database**
   - Create a MySQL database named `otp_gateway`
   - Update database credentials in `src/main/resources/application.yml`

3. **Configure Email Settings**
   - Open `src/main/resources/application.yml`
   - Update the following email configuration:
   ```yaml
   spring:
     mail:
       host: smtp.gmail.com
       port: 587
       username: your-email@gmail.com
       password: your-app-specific-password
   ```
   Note: For Gmail, use App-Specific Password. [Learn how to generate one](https://support.google.com/accounts/answer/185833?hl=en)

4. **Build the Project**
   ```bash
   mvn clean install
   ```

5. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

## API Documentation

### Authentication Endpoints

#### Register New User
```http
POST /api/v1/auth/register
```
Parameters:
- `email` (required): User's email address
- `password` (required): User's password
- `firstName` (required): User's first name
- `lastName` (required): User's last name

#### Verify Email
```http
POST /api/v1/auth/verify-email
```
Parameters:
- `email` (required): User's email address
- `otp` (required): OTP received in email

#### Login
```http
POST /api/v1/auth/authenticate
```
Parameters:
- `email` (required): User's email address
- `password` (required): User's password

Returns: JWT token

#### Forgot Password
```http
POST /api/v1/auth/forgot-password
```
Parameters:
- `email` (required): User's email address

#### Reset Password
```http
POST /api/v1/auth/reset-password
```
Parameters:
- `email` (required): User's email address
- `otp` (required): OTP received in email
- `newPassword` (required): New password

### OTP Endpoints (Protected)

#### Generate New OTP
```http
POST /api/v1/otp/generate
```
Headers:
- `Authorization`: Bearer {jwt-token}

#### Verify OTP
```http
POST /api/v1/otp/verify
```
Headers:
- `Authorization`: Bearer {jwt-token}

Parameters:
- `otp` (required): OTP to verify

## Security Features

1. **Password Security**
   - Passwords are encrypted using BCrypt
   - Configurable password strength requirements

2. **OTP Security**
   - Secure random OTP generation
   - Configurable OTP length
   - Time-based expiration
   - Rate limiting for attempts

3. **JWT Security**
   - Token-based authentication
   - Configurable token expiration
   - Secure token generation and validation

4. **API Security**
   - Protected endpoints
   - CSRF protection
   - Input validation
   - Rate limiting

## Configuration

The application can be configured through `application.yml`:

```yaml
application:
  security:
    jwt:
      secret-key: your-secret-key
      expiration: 86400000 # 24 hours in milliseconds
  otp:
    length: 6
    expiration: 300000 # 5 minutes in milliseconds
    rate-limit: 3 # maximum attempts allowed
```

## Error Handling

The system includes comprehensive error handling for:
- Invalid credentials
- Expired OTPs
- Rate limit exceeded
- Email sending failures
- Invalid tokens
- Database errors

## Development

### Project Structure
```
src/main/java/com/otp/gateway/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── model/          # Entity classes
├── repository/     # Data repositories
├── security/       # Security configurations
└── service/        # Business logic
```

### Building for Production

1. Update production configurations in `application-prod.yml`
2. Build the project:
   ```bash
   mvn clean package -Pprod
   ```
3. Run the JAR file:
   ```bash
   java -jar target/email-otp-gateway-1.0.0.jar --spring.profiles.active=prod
   ```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.