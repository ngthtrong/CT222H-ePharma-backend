# Hướng dẫn Fix Lỗi OAuth2: "The OAuth client was not found"

## Nguyên nhân lỗi
Lỗi `invalid_client` xảy ra vì:
1. Đang sử dụng fake credentials trong file .env
2. Google/Facebook không nhận diện được client ID
3. Client secret không đúng
4. Redirect URI không được authorized

## Các bước khắc phục

### Bước 1: Tạo Google OAuth2 Credentials
```
1. Truy cập: https://console.cloud.google.com/
2. Tạo project mới
3. Enable APIs: Google+ API hoặc People API  
4. Tạo OAuth2 credentials
5. Thêm redirect URI: http://localhost:8081/api/v1/auth/oauth2/callback/google
6. Copy Client ID và Client Secret
```

### Bước 2: Tạo Facebook OAuth2 Credentials  
```
1. Truy cập: https://developers.facebook.com/
2. Tạo app mới
3. Thêm Facebook Login product
4. Cấu hình redirect URI: http://localhost:8081/api/v1/auth/oauth2/callback/facebook
5. Copy App ID và App Secret
```

### Bước 3: Cập nhật file .env
```env
# Thay thế với credentials thật
GOOGLE_CLIENT_ID=your-real-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=your-real-client-secret
FACEBOOK_CLIENT_ID=your-real-app-id  
FACEBOOK_CLIENT_SECRET=your-real-app-secret
```

### Bước 4: Restart container
```bash
docker-compose down
docker-compose up --build -d
```

### Bước 5: Test lại
```bash
# Chạy script test
.\test-oauth2.ps1

# Hoặc test manual
curl http://localhost:8081/api/v1/auth/oauth2/login/google
```

## Lưu ý quan trọng

### Cho Development:
- Thêm email test vào Google OAuth consent screen
- Facebook app ở chế độ Development chỉ admin/dev có thể đăng nhập

### Cho Production:
- Submit Google app để verify (nếu cần)
- Submit Facebook app để review
- Cập nhật redirect URI với domain thật
- Sử dụng HTTPS

### Kiểm tra redirect URI:
- Google: http://localhost:8081/api/v1/auth/oauth2/callback/google
- Facebook: http://localhost:8081/api/v1/auth/oauth2/callback/facebook
- Phải khớp chính xác trong OAuth2 console

## Troubleshooting

### Nếu vẫn lỗi:
1. Kiểm tra Client ID có đúng format không
2. Kiểm tra redirect URI có chính xác không  
3. Kiểm tra app có ở chế độ development không
4. Clear browser cache và cookies
5. Kiểm tra logs container: `docker logs vegeta-backend`
