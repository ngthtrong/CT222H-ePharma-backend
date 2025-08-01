# Test OAuth2 Flow - Hướng dẫn

## Bước 1: Lấy Authorization URL
```bash
curl http://localhost:8081/api/v1/auth/oauth2/login/google
```

## Bước 2: Mở URL trong trình duyệt
Copy URL từ response và paste vào trình duyệt:
```
https://accounts.google.com/o/oauth2/v2/auth?client_id=347941085213-4jlqodvmf6ilkprkso4g34vudj7uddfl.apps.googleusercontent.com&redirect_uri=http://localhost:8081/api/v1/auth/oauth2/callback/google&scope=openid email profile&response_type=code&state=xxx
```

## Bước 3: Đăng nhập Google
1. Chọn tài khoản Google (jhinyugioh@gmail.com)
2. Cho phép quyền truy cập (nếu được hỏi)
3. Google sẽ redirect về: `http://localhost:8081/api/v1/auth/oauth2/callback/google?code=xxx&state=xxx`

## Bước 4: Kiểm tra callback
Backend sẽ tự động:
1. Nhận authorization code từ Google
2. Exchange code để lấy access token
3. Lấy thông tin user từ Google
4. Tạo/update user trong database
5. Tạo JWT token cho user
6. Trả về LoginResponse với JWT token

## Bước 5: Kiểm tra logs
```bash
docker logs vegeta-backend --tail 20
```

## Expected Response sau OAuth2 flow:
```json
{
  "success": true,
  "message": "Đăng nhập OAuth2 thành công",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": "xxx",
      "fullName": "User Name",
      "email": "jhinyugioh@gmail.com",
      "role": "USER",
      "authProvider": "GOOGLE"
    }
  }
}
```
