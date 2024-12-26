# ReelNet Backend

A Spring Boot application following Hexagonal Architecture (Ports and Adapters) pattern.

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/reelnet/
│   │   │       ├── api/                    # API Layer
│   │   │       ├── core/                   # Core Business Logic
│   │   │       ├── infrastructure/         # Infrastructure Layer
│   │   │       ├── application/           # Application Layer
│   │   │       └── common/                # Shared Components
│   │   └── resources/
│   └── test/
└── pom.xml
```

## Prerequisites

- Java 21
- Maven
- PostgreSQL (Supabase)

## Environment Variables

Create a `.env` file in the project root with the following variables:

```
SUPABASE_DB_URL=your_supabase_db_url
SUPABASE_USERNAME=your_supabase_username
SUPABASE_PASSWORD=your_supabase_password
SERVER_PORT=8080
```

## Running the Application

1. Set up environment variables
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Authentication
- POST `/api/v1/auth/register` - Register a new user

## Testing

Run tests using:
```bash
mvn test
``` 