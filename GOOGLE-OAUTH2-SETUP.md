# Hướng dẫn tạo Google OAuth2 Credentials

## Bước 1: Truy cập Google Cloud Console
1. Đi tới https://console.cloud.google.com/
2. Đăng nhập với tài khoản Google của bạn
3. Tạo project mới hoặc chọn project hiện có

## Bước 2: Kích hoạt Google+ API
1. Trong menu bên trái, chọn "APIs & Services" > "Library"
2. Tìm kiếm "Google+ API" hoặc "People API"
3. Nhấp vào và chọn "Enable"

## Bước 3: Tạo OAuth2 Credentials
1. Đi tới "APIs & Services" > "Credentials"
2. Nhấp "Create Credentials" > "OAuth client ID"
3. Chọn application type: "Web application"
4. Nhập tên cho OAuth client
5. Trong "Authorized redirect URIs", thêm:
   - http://localhost:8081/api/v1/auth/oauth2/callback/google
   - https://yourdomain.com/api/v1/auth/oauth2/callback/google (cho production)

## Bước 4: Lấy Client ID và Client Secret
1. Sau khi tạo xong, bạn sẽ thấy:
   - Client ID: dạng xxxxx.apps.googleusercontent.com
   - Client Secret: dạng chuỗi ký tự ngẫu nhiên
2. Copy và lưu lại 2 giá trị này

## Bước 5: Cấu hình OAuth Consent Screen
1. Đi tới "APIs & Services" > "OAuth consent screen"
2. Chọn "External" user type
3. Điền thông tin ứng dụng:
   - App name: Tên ứng dụng của bạn
   - User support email: Email hỗ trợ
   - Developer contact information: Email liên hệ
4. Trong "Scopes", thêm:
   - openid
   - email
   - profile
5. Lưu và tiếp tục

## Bước 6: Thêm Test Users (nếu app chưa được verify)
1. Trong "OAuth consent screen" > "Test users"
2. Thêm email của bạn vào danh sách test users
3. Chỉ những email này mới có thể đăng nhập khi app ở chế độ testing
