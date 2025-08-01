# Hướng dẫn tạo Facebook OAuth2 Credentials

## Bước 1: Truy cập Facebook Developers
1. Đi tới https://developers.facebook.com/
2. Đăng nhập với tài khoản Facebook của bạn
3. Nhấp "Get Started" nếu là lần đầu

## Bước 2: Tạo App mới
1. Nhấp "Create App" hoặc "My Apps" > "Create App"
2. Chọn "Consumer" use case
3. Nhập:
   - App Display Name: Tên ứng dụng của bạn
   - App Contact Email: Email liên hệ
4. Nhấp "Create App"

## Bước 3: Thêm Facebook Login Product
1. Trong dashboard của app, tìm "Facebook Login"
2. Nhấp "Set Up" hoặc "Add to App"
3. Chọn platform "Web"

## Bước 4: Cấu hình Facebook Login
1. Trong menu bên trái, chọn "Facebook Login" > "Settings"
2. Trong "Valid OAuth Redirect URIs", thêm:
   - http://localhost:8081/api/v1/auth/oauth2/callback/facebook
   - https://yourdomain.com/api/v1/auth/oauth2/callback/facebook (cho production)
3. Bật "Use Strict Mode for Redirect URIs"
4. Trong "Allowed Domains for the JavaScript SDK", thêm:
   - localhost (cho development)
   - yourdomain.com (cho production)

## Bước 5: Lấy App ID và App Secret
1. Trong menu bên trái, chọn "Settings" > "Basic"
2. Bạn sẽ thấy:
   - App ID: Dãy số ID của app
   - App Secret: Nhấp "Show" để xem (cần xác nhận mật khẩu Facebook)
3. Copy và lưu lại 2 giá trị này

## Bước 6: Cấu hình App Domain và Privacy Policy
1. Vẫn trong "Settings" > "Basic"
2. Điền:
   - App Domains: localhost, yourdomain.com
   - Privacy Policy URL: URL chính sách bảo mật của bạn
   - Terms of Service URL: URL điều khoản dịch vụ

## Bước 7: Chuyển App sang Live Mode (khi ready)
1. Trong dashboard, app sẽ ở chế độ "Development" 
2. Để public sử dụng, bạn cần submit app để Facebook review
3. Trong chế độ development, chỉ admin và developer của app có thể đăng nhập
