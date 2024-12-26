# Setup Guide

## Prerequisites

Before setting up ReelNet, ensure you have the following installed:

1. **Development Tools**
   - Java 17 or later
   - Node.js 18 or later
   - Maven 3.8+
   - Git
   - Docker & Docker Compose
   - PostgreSQL 14+
   - Redis 6+

2. **IDE Recommendations**
   - IntelliJ IDEA (for backend)
   - VS Code (for frontend)
   - Recommended extensions will be loaded from `.vscode/extensions.json`

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/reelnet/reelnet.git
cd reelnet
```

### 2. Environment Setup

Create environment files for both backend and frontend:

```bash
# Backend environment (.env)
cp backend/.env.example backend/.env

# Frontend environment
cp frontend/.env.example frontend/.env
```

Update the environment variables according to your setup:

```env
# Backend (.env)
DB_HOST=localhost
DB_PORT=5432
DB_NAME=reelnet
DB_USER=postgres
DB_PASSWORD=your_password

REDIS_HOST=localhost
REDIS_PORT=6379

JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=900000

AWS_ACCESS_KEY=your_aws_access_key
AWS_SECRET_KEY=your_aws_secret_key
AWS_REGION=your_aws_region
AWS_S3_BUCKET=your_s3_bucket

# Frontend (.env)
VITE_API_URL=http://localhost:8080/api
VITE_WS_URL=ws://localhost:8080/ws
```

### 3. Database Setup

```bash
# Start PostgreSQL and Redis using Docker
docker-compose up -d postgres redis

# Create database
psql -U postgres -c "CREATE DATABASE reelnet"

# Run migrations
cd backend
./mvnw flyway:migrate
```

### 4. Backend Setup

```bash
cd backend

# Install dependencies
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The backend will be available at `http://localhost:8080`

### 5. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

The frontend will be available at `http://localhost:5173`

## Project Structure

```
reelnet/
├── backend/                 # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   └── README.md
│
├── frontend/               # React frontend
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   └── services/
│   ├── package.json
│   └── README.md
│
├── docs/                  # Documentation
├── docker/                # Docker configurations
└── docker-compose.yml
```

## Development Workflow

### Backend Development

1. **Run in Development Mode**
   ```bash
   cd backend
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

2. **Run Tests**
   ```bash
   ./mvnw test
   ```

3. **API Documentation**
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Docs: `http://localhost:8080/v3/api-docs`

### Frontend Development

1. **Run in Development Mode**
   ```bash
   cd frontend
   npm run dev
   ```

2. **Run Tests**
   ```bash
   npm test
   ```

3. **Build for Production**
   ```bash
   npm run build
   ```

### Docker Development

Run the entire stack using Docker Compose:

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

## Common Development Tasks

### Database Management

1. **Create Migration**
   ```bash
   cd backend
   ./mvnw flyway:create -Dflyway.name="create_users_table"
   ```

2. **Reset Database**
   ```bash
   ./mvnw flyway:clean flyway:migrate
   ```

### Code Quality

1. **Backend Checks**
   ```bash
   # Run checkstyle
   ./mvnw checkstyle:check

   # Run spotbugs
   ./mvnw spotbugs:check
   ```

2. **Frontend Checks**
   ```bash
   # Run linter
   npm run lint

   # Run prettier
   npm run format
   ```

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Verify PostgreSQL is running: `docker ps`
   - Check connection settings in `.env`
   - Ensure database exists: `psql -U postgres -l`

2. **Redis Connection Issues**
   - Verify Redis is running: `docker ps`
   - Check Redis connection in `.env`
   - Test connection: `redis-cli ping`

3. **Frontend Build Issues**
   - Clear node modules: `rm -rf node_modules`
   - Reinstall dependencies: `npm install`
   - Clear cache: `npm cache clean --force`

4. **Backend Build Issues**
   - Clean Maven cache: `./mvnw clean`
   - Update dependencies: `./mvnw versions:display-dependency-updates`
   - Check Java version: `java -version`

### Logs

1. **Backend Logs**
   - Application logs: `backend/logs/application.log`
   - Spring Boot logs: `backend/spring.log`

2. **Frontend Logs**
   - Development server logs in terminal
   - Browser console for runtime errors

3. **Docker Logs**
   ```bash
   # View all container logs
   docker-compose logs

   # View specific service logs
   docker-compose logs backend
   docker-compose logs frontend
   ```

## Security Setup

1. **SSL/TLS Configuration**
   - Generate certificates
   - Configure HTTPS
   - Set up CORS properly

2. **Authentication Setup**
   - Configure JWT settings
   - Set up OAuth providers
   - Configure password policies

## Monitoring Setup

1. **Backend Monitoring**
   - Configure Spring Boot Actuator
   - Set up Prometheus metrics
   - Configure logging levels

2. **Frontend Monitoring**
   - Set up error tracking
   - Configure performance monitoring
   - Enable analytics

## Next Steps

After completing the setup:

1. Review the [Architecture Documentation](../architecture/overview.md)
2. Explore the [API Documentation](../api/)
3. Check the [Development Guidelines](./development.md)
4. Set up [CI/CD](./deployment.md)

For additional help:
- Join our [Discord community](https://discord.gg/reelnet)
- Check our [Issue Tracker](https://github.com/reelnet/reelnet/issues)
- Read our [Contributing Guide](../CONTRIBUTING.md) 