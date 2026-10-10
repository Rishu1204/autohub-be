# AutoHub Spring Boot API

Spring Boot REST API paired with `../autohub-site`. It accepts booking requests, stores them in an H2 file database for local development, and emails the configured inbox using Gmail SMTP. The code is layered so new features can be added in controller/service/repository packages.

## Requirements

- Java 21
- Maven 3.9+ (or import `pom.xml` in IntelliJ and use its Maven wrapper if you add one)
- A Gmail account with 2-Step Verification enabled and a Google App Password

## Configure Gmail SMTP

1. Enable Google 2-Step Verification on the Gmail account that will send mail.
2. Create an **App Password** in Google Account security settings. Use this app password, not your regular Gmail password. If App Passwords is unavailable, your account or organisation may not permit it.
3. Copy `.env.example` values into your IDE run configuration or shell environment. Spring Boot does not automatically load `.env` files, so export the variables or set them in IntelliJ Run Configuration → Environment variables.

Example shell setup (Linux/macOS):

```bash
export MAIL_USERNAME='yourgmail@gmail.com'
export MAIL_APP_PASSWORD='your-16-character-app-password'
export BOOKING_TO_EMAIL='your-inbox@example.com'
export CORS_ALLOWED_ORIGINS='http://localhost:5173,https://your-site.vercel.app'
./mvnw spring-boot:run # if you add/use Maven Wrapper
# or: mvn spring-boot:run
```

Never commit Gmail credentials, app passwords, or `.env` files. In production, set them as deployment environment variables/secrets.

## Run locally

```bash
mvn spring-boot:run
```

Health check: `http://localhost:8080/actuator/health`

## API

`POST http://localhost:8080/api/v1/bookings`

```json
{
  "name": "Jane Customer",
  "email": "jane@example.com",
  "phone": "+91 9876543210",
  "service": "Repair & Maintenance",
  "date": "2026-11-02",
  "message": "Please check the brakes."
}
```

Success returns HTTP `201 Created`, for example:

```json
{
  "id": 1,
  "status": "RECEIVED",
  "message": "Your booking request has been received.",
  "createdAt": "2026-10-10T10:30:00Z"
}
```

Bookings are saved even if Gmail notification delivery fails; check server logs for mail errors. For production, add a persistent managed database (for example PostgreSQL), migrations, authentication for any admin endpoints, rate limiting, monitoring, and privacy/retention controls before exposing booking data.

## Connect React

In `autohub-site/.env.local`, set:

```bash
VITE_API_BASE_URL=http://localhost:8080
```

Vite must be restarted after changing env files. When the React app is deployed to Vercel, set `VITE_API_BASE_URL` to the public base URL of this Spring Boot service, and set `CORS_ALLOWED_ORIGINS` on the API to the exact deployed frontend origin(s). Deploying React to Vercel does not automatically host this Spring Boot API; deploy the API to a Java-capable host/container service separately.
