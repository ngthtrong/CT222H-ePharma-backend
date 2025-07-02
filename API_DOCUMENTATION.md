# API Documentation - Project Back End

## Thông tin cơ bản
- **Base URL**: `http://localhost:8081`
- **Database**: MongoDB Atlas
- **Framework**: Spring Boot 3.5.3

## 1. API Test Connection

### GET /test
Kiểm tra kết nối cơ bản với MongoDB
```
GET http://localhost:8081/test
```

### GET /test-detailed  
Kiểm tra kết nối chi tiết với MongoDB
```
GET http://localhost:8081/test-detailed
```

### POST /test-insert
Thêm dữ liệu test đơn giản vào MongoDB
```
POST http://localhost:8081/test-insert
```

### GET /test-count
Đếm số lượng documents test trong database
```
GET http://localhost:8081/test-count
```

## 2. User APIs

### POST /api/users
Tạo user mới
```
POST http://localhost:8081/api/users
Content-Type: application/json

{
  "name": "Nguyễn Văn A",
  "email": "a@example.com",
  "age": 25,
  "phone": "0123456789"
}
```

### POST /api/users/batch
Tạo nhiều users cùng lúc
```
POST http://localhost:8081/api/users/batch
Content-Type: application/json

[
  {
    "name": "Nguyễn Văn A",
    "email": "a@example.com",
    "age": 25,
    "phone": "0123456789"
  },
  {
    "name": "Trần Thị B", 
    "email": "b@example.com",
    "age": 30,
    "phone": "0987654321"
  }
]
```

### POST /api/users/sample-data
Tạo dữ liệu mẫu để test (5 users)
```
POST http://localhost:8081/api/users/sample-data
```

### GET /api/users
Lấy danh sách tất cả users
```
GET http://localhost:8081/api/users
```

### GET /api/users/{id}
Lấy user theo ID
```
GET http://localhost:8081/api/users/66850a1b2c4f5a001b2c3d4e
```

### GET /api/users/search?name={name}
Tìm kiếm user theo tên
```
GET http://localhost:8081/api/users/search?name=Nguyễn
```

### PUT /api/users/{id}
Cập nhật thông tin user
```
PUT http://localhost:8081/api/users/66850a1b2c4f5a001b2c3d4e
Content-Type: application/json

{
  "name": "Nguyễn Văn A Updated",
  "email": "a_updated@example.com", 
  "age": 26,
  "phone": "0123456790"
}
```

### DELETE /api/users/{id}
Xóa user theo ID
```
DELETE http://localhost:8081/api/users/66850a1b2c4f5a001b2c3d4e
```

### GET /api/users/count
Đếm tổng số users trong database
```
GET http://localhost:8081/api/users/count
```

## 3. Response Format

Tất cả API đều trả về theo format:
```json
{
  "success": true/false,
  "message": "Thông báo",
  "data": {...} // Dữ liệu trả về (có thể null)
}
```

## 4. Các bước test API

### Bước 1: Test kết nối
```bash
# Kiểm tra server có chạy không
curl http://localhost:8081/test

# Kiểm tra kết nối MongoDB
curl http://localhost:8081/test-detailed
```

### Bước 2: Tạo dữ liệu mẫu
```bash
# Tạo dữ liệu mẫu
curl -X POST http://localhost:8081/api/users/sample-data
```

### Bước 3: Xem dữ liệu
```bash
# Lấy danh sách users
curl http://localhost:8081/api/users

# Đếm số lượng users
curl http://localhost:8081/api/users/count
```

### Bước 4: Tạo user mới
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "age": 25,
    "phone": "0123456789"
  }'
```

### Bước 5: Test insert dữ liệu đơn giản
```bash
# Insert test data
curl -X POST http://localhost:8081/test-insert

# Kiểm tra số lượng test data
curl http://localhost:8081/test-count
```

## 5. Error Codes

- **200**: Success
- **400**: Bad Request (dữ liệu không hợp lệ)
- **404**: Not Found (không tìm thấy resource)
- **500**: Internal Server Error (lỗi server)

## 6. Notes

- Email phải unique (không được trùng)
- Tất cả field trong UserCreateRequest đều required
- API hỗ trợ CORS từ mọi nguồn
- Database sử dụng MongoDB Atlas với collection "users"
