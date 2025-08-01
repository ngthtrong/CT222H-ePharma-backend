

---

# 🛒 WellVerse E-commerce Backend API

## 🔐 Authentication Options
Dự án hỗ trợ **3 phương thức đăng nhập**:
- **Traditional**: Email + Password
- **Google OAuth2**: Đăng nhập với tài khoản Google  
- **Facebook OAuth2**: Đăng nhập với tài khoản Facebook

## 🚀 Quick Start

### Chạy ứng dụng với Docker (Khuyến nghị)
```bash
# Setup OAuth2 credentials
cp .env.example .env
# Edit .env file với Google và Facebook credentials

# Build và start containers
docker-compose up --build -d

# Check application status
docker-compose ps
```

### Chạy ứng dụng trực tiếp
```bash
./mvnw spring-boot:run
```

### Test OAuth2
```powershell
# Windows
./test-oauth2.ps1

# Linux/Mac
chmod +x test-oauth2.sh
./test-oauth2.sh
```

### Setup Guides
- **Docker + OAuth2**: [DOCKER-OAUTH2-SETUP.md](./DOCKER-OAUTH2-SETUP.md) ⭐ **Khuyến nghị**
- **OAuth2 Backend**: [OAuth2-Backend-Setup.md](./OAuth2-Backend-Setup.md)
- **OAuth2 Frontend**: [OAuth2-Frontend-Guide.md](./OAuth2-Frontend-Guide.md)

--- 

## 1. Quy ước chung

-   **Base URL**: Tất cả các API sẽ có tiền tố là `/api/v1`. Ví dụ: `/api/v1/products`.
-   **Định dạng dữ liệu**: Tất cả request body và response body đều ở định dạng `JSON`.
-   **Xác thực**:
    -   Các API yêu cầu xác thực người dùng đã đăng nhập sẽ cần một `Authorization` header chứa `Bearer Token` (JWT).
    -   Các API liên quan đến giỏ hàng của khách vãng lai sẽ yêu cầu một header tùy chỉnh là `X-Cart-Session-ID`, chứa ID phiên duy nhất do frontend tạo và quản lý.
-   **Phân quyền**:
    -   `PUBLIC`: Bất kỳ ai cũng có thể truy cập.
    -   `USER`: Yêu cầu đăng nhập với vai trò `USER` hoặc `ADMIN`.
    -   `ADMIN`: Yêu cầu đăng nhập với vai trò `ADMIN`.
    -   `PUBLIC / USER`: API có thể được gọi bởi cả người dùng đã đăng nhập và khách vãng lai. Hệ thống sẽ dựa vào sự hiện diện của `Authorization` header hoặc `X-Cart-Session-ID` header để xử lý.
-   **Mã trạng thái HTTP (Status Codes)**:
    -   `200 OK`: Yêu cầu thành công.
    -   `201 Created`: Tạo mới tài nguyên thành công.
    -   `400 Bad Request`: Dữ liệu gửi lên không hợp lệ.
    -   `401 Unauthorized`: Chưa xác thực (chưa đăng nhập hoặc token không hợp lệ).
    -   `403 Forbidden`: Không có quyền truy cập tài nguyên.
    -   `404 Not Found`: Không tìm thấy tài nguyên.
    -   `500 Internal Server Error`: Lỗi từ phía server.

---

## 2. API Xác thực (Authentication)

### Traditional Authentication
| Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/register` | `PUBLIC` | Đăng ký tài khoản mới bằng email và mật khẩu |
| `POST` | `/auth/login` | `PUBLIC` | Đăng nhập bằng email và mật khẩu, trả về JWT token |
| `POST` | `/auth/logout` | `USER` | Đăng xuất và blacklist JWT token |
| `POST` | `/auth/forgot-password` | `PUBLIC` | Yêu cầu đặt lại mật khẩu |
| `POST` | `/auth/reset-password` | `PUBLIC` | Đặt lại mật khẩu với token |

### OAuth2 Authentication 🆕
| Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- |
| `GET` | `/auth/oauth2/login/google` | `PUBLIC` | Lấy URL đăng nhập Google OAuth2 |
| `GET` | `/auth/oauth2/login/facebook` | `PUBLIC` | Lấy URL đăng nhập Facebook OAuth2 |
| `POST` | `/auth/oauth2/callback/google` | `PUBLIC` | Xử lý callback từ Google, trả về JWT token |
| `POST` | `/auth/oauth2/callback/facebook` | `PUBLIC` | Xử lý callback từ Facebook, trả về JWT token |
| `GET` | `/auth/oauth2/status` | `PUBLIC` | Kiểm tra trạng thái cấu hình OAuth2 |

---

## 3. API Người dùng (Users)

| Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| **GĐ 1** | `GET` | `/users/me` | `USER` | Lấy thông tin chi tiết của người dùng đang đăng nhập. |
| **GĐ 1** | `PUT` | `/users/me` | `USER` | Cập nhật thông tin cơ bản của người dùng (fullName, phoneNumber). |
| **GĐ 1** | `GET` | `/users/me/addresses` | `USER` | Lấy danh sách địa chỉ của người dùng. |
| **GĐ 1** | `POST` | `/users/me/addresses` | `USER` | Thêm một địa chỉ mới cho người dùng. |
| **GĐ 1** | `PUT` | `/users/me/addresses/:addressId` | `USER` | Cập nhật một địa chỉ đã có. |
| **GĐ 1** | `DELETE` | `/users/me/addresses/:addressId` | `USER` | Xóa một địa chỉ. |
| **GĐ 1** | `PATCH` | `/users/me/addresses/:addressId/default` | `USER` | Đặt một địa chỉ làm địa chỉ mặc định. |
| **GĐ 1** | `GET` | `/admin/users` | `ADMIN` | **(Admin)** Lấy danh sách tất cả người dùng (có phân trang). |
| **GĐ 1** | `DELETE` | `/admin/users/:userId` | `ADMIN` | **(Admin)** Xóa một người dùng. |

---

## 4. API Danh mục sản phẩm (Categories)

| Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| **GĐ 1** | `GET` | `/categories` | `PUBLIC` | Lấy tất cả danh mục. Nên trả về dạng cây (cha-con). |
| **GĐ 1** | `GET` | `/categories/:slug` | `PUBLIC` | Lấy thông tin chi tiết một danh mục theo slug. |
| **GĐ 1** | `POST` | `/admin/categories` | `ADMIN` | **(Admin)** Tạo một danh mục mới. |
| **GĐ 1** | `PUT` | `/admin/categories/:id` | `ADMIN` | **(Admin)** Cập nhật một danh mục. |
| **GĐ 1** | `DELETE` | `/admin/categories/:id` | `ADMIN` | **(Admin)** Xóa một danh mục. |

---

## 5. API Sản phẩm (Products)

| Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| **GĐ 1** | `GET` | `/products` | `PUBLIC` | Lấy danh sách sản phẩm (phân trang, sắp xếp, bộ lọc). <br> **Query Params:** `page`, `limit`, `sortBy`, `order`, `categoryId`, `minPrice`, `maxPrice`. |
| **GĐ 1** | `GET` | `/products/search` | `PUBLIC` | Tìm kiếm sản phẩm. <br> **Query Params:** `q`. |
| **GĐ 1** | `GET` | `/products/:slug` | `PUBLIC` | Lấy thông tin chi tiết một sản phẩm theo slug. |
| **GĐ 1** | `GET` | `/products/:id/related` | `PUBLIC` | Lấy danh sách sản phẩm liên quan. |
| **GĐ 1** | `POST` | `/admin/products` | `ADMIN` | **(Admin)** Tạo sản phẩm mới. |
| **GĐ 1** | `PUT` | `/admin/products/:id` | `ADMIN` | **(Admin)** Cập nhật thông tin sản phẩm. |
| **GĐ 1** | `DELETE` | `/admin/products/:id` | `ADMIN` | **(Admin)** Xóa một sản phẩm. |
| **GĐ 2** | `GET` | `/products/:id/reviews` | `PUBLIC` | Lấy tất cả đánh giá của một sản phẩm. |

---

## 6. API Giỏ hàng (Cart) - **ĐÃ CẬP NHẬT**

**Lưu ý:** Giỏ hàng được quản lý trong một collection riêng. Các API này hoạt động cho cả người dùng đã đăng nhập (dựa trên `accessToken`) và khách vãng lai (dựa trên header `X-Cart-Session-ID`).

| Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| **GĐ 1** | `GET` | `/cart` | `PUBLIC / USER` | Lấy thông tin giỏ hàng hiện tại. |
| **GĐ 1** | `POST` | `/cart/items` | `PUBLIC / USER` | Thêm một sản phẩm vào giỏ hàng. <br> **Request Body:** `{ "productId": "...", "quantity": 1 }` |
| **GĐ 1** | `PUT` | `/cart/items/:productId` | `PUBLIC / USER` | Cập nhật số lượng của một sản phẩm trong giỏ hàng. <br> **Request Body:** `{ "quantity": 2 }` |
| **GĐ 1** | `DELETE` | `/cart/items/:productId` | `PUBLIC / USER` | Xóa một sản phẩm khỏi giỏ hàng. |
| **GĐ 1** | `DELETE` | `/cart` | `PUBLIC / USER` | Xóa toàn bộ sản phẩm trong giỏ hàng. |
| **GĐ 1** | **(MỚI)** `POST` | `/cart/merge` | `USER` | **(Quan trọng)** Gộp giỏ hàng của khách vào tài khoản người dùng sau khi đăng nhập. Yêu cầu có `X-Cart-Session-ID` header. |

---

## 7. API Đơn hàng (Orders)

| Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| **GĐ 1** | `POST` | `/orders` | `USER` | Tạo đơn hàng mới từ giỏ hàng hiện tại. <br> **Request Body:** `{ "shippingAddressId": "...", "notes": "Giao giờ hành chính" }` |
| **GĐ 1** | `GET` | `/orders` | `USER` | Lấy lịch sử mua hàng của người dùng (có phân trang). |
| **GĐ 1** | `GET` | `/orders/:orderCode` | `USER` | Lấy chi tiết một đơn hàng của người dùng. |
| **GĐ 1** | `PATCH` | `/orders/:orderCode/cancel` | `USER` | Người dùng tự hủy đơn hàng (nếu trạng thái cho phép). |
| **GĐ 1** | `GET` | `/admin/orders` | `ADMIN` | **(Admin)** Lấy danh sách tất cả đơn hàng (có phân trang, lọc). |
| **GĐ 1** | `GET` | `/admin/orders/:orderCode` | `ADMIN` | **(Admin)** Lấy chi tiết một đơn hàng bất kỳ. |
| **GĐ 1** | `PATCH` | `/admin/orders/:id/status` | `ADMIN` | **(Admin)** Cập nhật trạng thái đơn hàng. <br> **Request Body:** `{ "status": "PROCESSING" }` |
| **GĐ 2** | `POST` | `/orders/checkout/momo` | `USER` | **(Thanh toán thật)** Tạo yêu cầu thanh toán qua Momo. |

---

## 8. API Đánh giá & Phản hồi (Reviews)

| Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| **GĐ 2** | `POST` | `/reviews` | `USER` | Gửi đánh giá cho một sản phẩm đã mua. <br> **Request Body:** `{ "productId": "...", "rating": 5, "comment": "..." }` |
| **GĐ 2** | `PUT` | `/admin/reviews/:id/reply` | `ADMIN` | **(Admin)** Trả lời một đánh giá của người dùng. <br> **Request Body:** `{ "responseText": "..." }` |

---

## 9. API Khuyến mãi (Promotions)

| Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| **GĐ 1** | N/A | N/A | N/A | **Chỉ quản lý `discountPercent`** trong API của Sản phẩm. |
| **GĐ 2** | `GET` | `/promotions` | `PUBLIC` | Lấy danh sách các chương trình khuyến mãi đang hoạt động. |
| **GĐ 2** | `POST` | `/admin/promotions` | `ADMIN` | **(Admin)** Tạo một chương trình khuyến mãi mới. |
| **GĐ 2** | `PUT` | `/admin/promotions/:id` | `ADMIN` | **(Admin)** Cập nhật một chương trình khuyến mãi. |
| **GĐ 2** | `DELETE` | `/admin/promotions/:id` | `ADMIN` | **(Admin)** Xóa một chương trình khuyến mãi. |

---

## 10. API Báo cáo & Thống kê (Reports)

| Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| **GĐ 1** | `GET` | `/admin/reports/revenue` | `ADMIN` | **(Admin)** Lấy báo cáo doanh thu. <br> **Query Params:** `period` (DAILY, MONTHLY) hoặc `startDate` & `endDate`. |

---

## 11. API Lịch sử tìm kiếm (Search History) & Thông báo (Notifications)

| Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| **GĐ 2** | `GET` | `/search-history` | `USER` | Lấy lịch sử các từ khóa tìm kiếm gần đây của người dùng. |
| **GĐ 2** | `GET` | `/notifications` | `USER` | Lấy danh sách thông báo của người dùng. |
| **GĐ 2** | `POST` | `/notifications/:id/read` | `USER` | Đánh dấu một thông báo là đã đọc. |