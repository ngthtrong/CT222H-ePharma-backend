# Vegeta Backend - Docker Setup

This project is now configured to run with Docker while connecting to MongoDB Atlas.

## Prerequisites

- Docker Desktop installed and running
- Docker Compose available
- Internet connection (for MongoDB Atlas)

## Quick Start

### Windows
```batch
# Start the application
docker-start.bat

# Stop the application  
docker-stop.bat
```

### Manual Commands
```bash
# Build and start
docker-compose up --build -d

# Stop
docker-compose down

# View logs
docker-compose logs -f

# Rebuild image
docker-compose build --no-cache
```

## Configuration

The application uses the following configuration:

- **Port**: 8081
- **Database**: MongoDB Atlas (existing connection)
- **Profile**: docker
- **Health Check**: `/actuator/health`

## Environment Variables

The following environment variables are configured in `docker-compose.yml`:

- `SPRING_PROFILES_ACTIVE=docker`
- `SPRING_DATA_MONGODB_URI` - MongoDB Atlas connection string
- `SPRING_DATA_MONGODB_DATABASE=Vegeta`
- `JWT_SECRET` - JWT signing secret
- `JWT_EXPIRATION=3600000` - JWT expiration time (1 hour)

## URLs

Once the application is running:

- **API Base**: http://localhost:8081
- **Health Check**: http://localhost:8081/actuator/health  
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **API Docs**: http://localhost:8081/v3/api-docs

## Docker Files

- `Dockerfile` - Multi-stage build for the Spring Boot application
- `docker-compose.yml` - Service configuration
- `.dockerignore` - Files to exclude from Docker build
- `application-docker.properties` - Docker-specific configuration

## Troubleshooting

### Check application status
```bash
docker-compose ps
docker-compose logs vegeta-backend
```

### Restart application
```bash
docker-compose restart vegeta-backend
```

### Clean rebuild
```bash
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### View health status
```bash
curl http://localhost:8081/actuator/health
```

## Notes

- The application connects to your existing MongoDB Atlas cluster
- No local MongoDB container is needed
- The Docker image is optimized with multi-stage build
- Health checks are configured for monitoring
- Non-root user is used for security
