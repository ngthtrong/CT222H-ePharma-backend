# 🐳 Docker Setup Guide với OAuth2

## 📋 Mục lục
- [Tổng quan](#tổng-quan)
- [Setup OAuth2 Credentials](#setup-oauth2-credentials)
- [Docker Configuration](#docker-configuration)
- [Running the Application](#running-the-application)
- [Testing OAuth2](#testing-oauth2)
- [Troubleshooting](#troubleshooting)

## 🎯 Tổng quan

Dự án đã được cấu hình để chạy với Docker và hỗ trợ OAuth2 authentication với Google và Facebook. Docker compose sẽ tự động:
- Build Spring Boot application với OAuth2 dependencies
- Setup environment variables cho OAuth2
- Expose port 8081 cho API access
- Configure health checks

## 🔧 Setup OAuth2 Credentials

### 1. Tạo file .env
```bash
# Copy template file
cp .env.example .env

# Edit với credentials của bạn
notepad .env  # Windows
nano .env     # Linux/Mac
```

### 2. Cấu hình Google OAuth2
1. Truy cập [Google Cloud Console](https://console.cloud.google.com/)
2. Tạo project mới hoặc chọn project hiện có
3. Enable Google+ API và Google OAuth2 API
4. Tạo OAuth2.0 credentials:
   - **Application type**: Web application
   - **Authorized redirect URIs**: `http://localhost:8081/api/v1/auth/oauth2/callback/google`
5. Copy Client ID và Client Secret vào file `.env`

### 3. Cấu hình Facebook OAuth2
1. Truy cập [Facebook Developers](https://developers.facebook.com/)
2. Tạo app mới
3. Thêm Facebook Login product
4. Cấu hình Valid OAuth Redirect URIs: `http://localhost:8081/api/v1/auth/oauth2/callback/facebook`
5. Copy App ID và App Secret vào file `.env`

### 4. File .env Example
```bash
# Google OAuth2
GOOGLE_CLIENT_ID=123456789-abcdefghijklmnop.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=GOCSPX-xxxxxxxxxxxxxxxxxxxxxxxx

# Facebook OAuth2
FACEBOOK_CLIENT_ID=123456789012345
FACEBOOK_CLIENT_SECRET=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

# OAuth2 Redirect URI (optional)
OAUTH2_REDIRECT_URI=http://localhost:8081/api/v1/auth/oauth2/callback
```

## 🐳 Docker Configuration

### docker-compose.yml
Project đã được cấu hình với Docker Compose:

```yaml
version: '3.8'

services:
  vegeta-backend:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: vegeta-backend
    ports:
      - "8081:8081"
    environment:
      # Existing config
      - SPRING_PROFILES_ACTIVE=docker
      - JWT_SECRET=...
      
      # OAuth2 Configuration
      - GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID:-}
      - GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET:-}
      - FACEBOOK_CLIENT_ID=${FACEBOOK_CLIENT_ID:-}
      - FACEBOOK_CLIENT_SECRET=${FACEBOOK_CLIENT_SECRET:-}
      - OAUTH2_REDIRECT_URI=${OAUTH2_REDIRECT_URI:-http://localhost:8081/api/v1/auth/oauth2/callback}
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8081/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s
```

### Dockerfile
Dockerfile đã được tối ưu để:
- Build application với Maven
- Create multi-stage build để giảm image size
- Setup health checks
- Use non-root user cho security

## 🚀 Running the Application

### 1. Build và Start Containers
```bash
# Build và start containers
docker-compose up --build -d

# Chỉ start (nếu đã build)
docker-compose up -d
```

### 2. Kiểm tra Status
```bash
# Check container status
docker-compose ps

# Check application health
curl http://localhost:8081/actuator/health

# View logs
docker-compose logs vegeta-backend

# Follow logs
docker-compose logs -f vegeta-backend
```

### 3. Stop Containers
```bash
# Stop containers
docker-compose down

# Stop và remove volumes
docker-compose down -v

# Stop và remove images
docker-compose down --rmi all
```

## 🧪 Testing OAuth2

### 1. Test với PowerShell (Windows)
```powershell
# Make script executable and run
./test-oauth2.ps1
```

### 2. Test với Bash (Linux/Mac)
```bash
# Make script executable and run
chmod +x test-oauth2.sh
./test-oauth2.sh
```

### 3. Manual API Testing

#### Test OAuth2 Status
```bash
curl -X GET http://localhost:8081/api/v1/auth/oauth2/status
```

#### Get Google Authorization URL
```bash
curl -X GET http://localhost:8081/api/v1/auth/oauth2/login/google
```

#### Get Facebook Authorization URL
```bash
curl -X GET http://localhost:8081/api/v1/auth/oauth2/login/facebook
```

### 4. Complete OAuth2 Flow Testing
1. Call `/oauth2/login/google` để lấy authorization URL
2. Open URL trong browser
3. Complete Google OAuth flow
4. Extract authorization code từ callback URL
5. Send POST request to `/oauth2/callback/google` với code

## 📱 Swagger Documentation
Truy cập API documentation tại: http://localhost:8081/swagger-ui.html

OAuth2 endpoints sẽ xuất hiện trong section "OAuth2 Authentication".

## 🐛 Troubleshooting

### Common Issues

#### 1. Container không start
```bash
# Check logs
docker-compose logs vegeta-backend

# Check Docker status
docker-compose ps

# Rebuild image
docker-compose up --build -d
```

#### 2. OAuth2 credentials không work
```bash
# Check environment variables trong container
docker-compose exec vegeta-backend env | grep -E "(GOOGLE|FACEBOOK)"

# Verify .env file
cat .env

# Restart containers sau khi update .env
docker-compose down && docker-compose up -d
```

#### 3. Port 8081 đã được sử dụng
```bash
# Check what's using port 8081
netstat -tulpn | grep 8081  # Linux
netstat -ano | findstr 8081  # Windows

# Stop other services hoặc change port trong docker-compose.yml
```

#### 4. Database connection issues
```bash
# Check MongoDB connection
docker-compose logs vegeta-backend | grep -i mongo

# Test MongoDB URI
# Ensure MongoDB Atlas whitelist IP 0.0.0.0/0 for Docker
```

#### 5. Health check failures
```bash
# Check health endpoint
curl http://localhost:8081/actuator/health

# Check if application started completely
docker-compose logs vegeta-backend | grep "Started ProjectBackEndApplication"
```

### Debug Mode
Enable debug logging trong docker-compose.yml:
```yaml
environment:
  - LOGGING_LEVEL_CT222H_VEGETA_PROJECTBACKEND_SERVICE_OAUTH2USERSERVICE=DEBUG
  - LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY=DEBUG
```

### Environment Variables Validation
Container sẽ log warning nếu OAuth2 credentials missing:
```bash
# Check for OAuth2 warnings
docker-compose logs vegeta-backend | grep -i oauth
```

## 🔒 Security Notes

### Production Considerations
1. **HTTPS**: Use HTTPS trong production
2. **Environment Variables**: Store credentials securely (không commit .env file)
3. **Network Security**: Use Docker networks cho internal communication
4. **Image Security**: Scan Docker images for vulnerabilities

### Example Production docker-compose.yml
```yaml
version: '3.8'

services:
  vegeta-backend:
    image: your-registry/vegeta-backend:latest
    container_name: vegeta-backend-prod
    ports:
      - "8081:8081"
    environment:
      - SPRING_PROFILES_ACTIVE=production
      - OAUTH2_REDIRECT_URI=https://api.yourdomain.com/api/v1/auth/oauth2/callback
      # Use Docker secrets cho production
    secrets:
      - google_client_id
      - google_client_secret
      - facebook_client_id
      - facebook_client_secret
    restart: unless-stopped
    
secrets:
  google_client_id:
    external: true
  google_client_secret:
    external: true
  facebook_client_id:
    external: true
  facebook_client_secret:
    external: true
```

## 📞 Support

Nếu gặp vấn đề:
1. Check container logs: `docker-compose logs vegeta-backend`
2. Verify OAuth2 provider configuration
3. Test với Postman hoặc curl
4. Check .env file configuration
5. Ensure Docker và Docker Compose đã cài đặt đúng

---

**Happy containerizing! 🐳🎉**
