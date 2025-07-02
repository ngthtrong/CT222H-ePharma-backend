---

# Tài liệu API cho dự án WellVerse

## 1. Quy ước chung

-   **Base URL**: Tất cả các API sẽ có tiền tố là `/api/v1`. Ví dụ: `/api/v1/products`.
-   **Định dạng dữ liệu**: Tất cả request body và response body đều ở định dạng `JSON`.
-   **Xác thực**: Các API yêu cầu xác thực sẽ cần một `Authorization` header chứa `Bearer Token` (JWT) sau khi người dùng đăng nhập.
-   **Phân quyền**:
    -   `PUBLIC`: Bất kỳ ai cũng có thể truy cập.
    -   `USER`: Yêu cầu đăng nhập với vai trò `USER` hoặc `ADMIN`.
    -   `ADMIN`: Yêu cầu đăng nhập với vai trò `ADMIN`.
-   **Mã trạng thái HTTP (Status Codes)**:
    -   `200 OK`: Yêu cầu thành công.
    -   `201 Created`: Tạo mới tài nguyên thành công.
    -   `400 Bad Request`: Dữ liệu gửi lên không hợp lệ.
    -   `401 Unauthorized`: Chưa xác thực (chưa đăng nhập).
    -   `403 Forbidden`: Không có quyền truy cập tài nguyên.
    -   `404 Not Found`: Không tìm thấy tài nguyên.
    -   `500 Internal Server Error`: Lỗi từ phía server.

---

## 2. API Xác thực (Authentication)

| Done | Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- | :--- |
| [ ] | **GĐ 1** | `POST` | `/auth/register` | `PUBLIC` | Đăng ký tài khoản mới bằng email và mật khẩu. |
| [ ] | **GĐ 1** | `POST` | `/auth/login` | `PUBLIC` | Đăng nhập bằng email và mật khẩu, trả về `accessToken`. |
| [ ] | **GĐ 2** | `GET` | `/auth/oauth2/google` | `PUBLIC` | Bắt đầu luồng đăng nhập với Google. (Redirect tới trang xác thực của Google) |
| [ ] | **GĐ 2** | `GET` | `/auth/oauth2/facebook` | `PUBLIC` | Bắt đầu luồng đăng nhập với Facebook. (Redirect tới trang xác thực của Facebook) |
| [ ] | **GĐ 2** | `GET` | `/auth/oauth2/callback/*` | `PUBLIC` | Endpoint nhận callback từ Google/Facebook sau khi người dùng xác thực. |

---

## 3. API Người dùng (Users)

| Done | Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- | :--- |
| [ ] | **GĐ 1** | `GET` | `/users/me` | `USER` | Lấy thông tin chi tiết của người dùng đang đăng nhập. |
| [ ] | **GĐ 1** | `PUT` | `/users/me` | `USER` | Cập nhật thông tin cơ bản của người dùng (fullName, phoneNumber). |
| [ ] | **GĐ 1** | `GET` | `/users/me/addresses` | `USER` | Lấy danh sách địa chỉ của người dùng. |
| [ ] | **GĐ 1** | `POST` | `/users/me/addresses` | `USER` | Thêm một địa chỉ mới cho người dùng. |
| [ ] | **GĐ 1** | `PUT` | `/users/me/addresses/:addressId` | `USER` | Cập nhật một địa chỉ đã có. |
| [ ] | **GĐ 1** | `DELETE` | `/users/me/addresses/:addressId` | `USER` | Xóa một địa chỉ. |
| [ ] | **GĐ 1** | `PATCH` | `/users/me/addresses/:addressId/default` | `USER` | Đặt một địa chỉ làm địa chỉ mặc định. |
| [ ] | **GĐ 1** | `GET` | `/admin/users` | `ADMIN` | **(Admin)** Lấy danh sách tất cả người dùng (có phân trang). |
| [ ] | **GĐ 1** | `DELETE` | `/admin/users/:userId` | `ADMIN` | **(Admin)** Xóa một người dùng. |

---

## 4. API Danh mục sản phẩm (Categories)

| Done | Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- | :--- |
| [ ] | **GĐ 1** | `GET` | `/categories` | `PUBLIC` | Lấy tất cả danh mục. Nên trả về dạng cây (cha-con). |
| [ ] | **GĐ 1** | `GET` | `/categories/:slug` | `PUBLIC` | Lấy thông tin chi tiết một danh mục theo slug. |
| [ ] | **GĐ 1** | `POST` | `/admin/categories` | `ADMIN` | **(Admin)** Tạo một danh mục mới. |
| [ ] | **GĐ 1** | `PUT` | `/admin/categories/:id` | `ADMIN` | **(Admin)** Cập nhật một danh mục. |
| [ ] | **GĐ 1** | `DELETE` | `/admin/categories/:id` | `ADMIN` | **(Admin)** Xóa một danh mục. |

---

## 5. API Sản phẩm (Products)

| Done | Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- | :--- |
| [ ] | **GĐ 1** | `GET` | `/products` | `PUBLIC` | Lấy danh sách sản phẩm (có phân trang, sắp xếp, và bộ lọc). <br> **Query Params:** `page`, `limit`, `sortBy` (price, name), `order` (asc, desc), `categoryId`, `minPrice`, `maxPrice`. |
| [ ] | **GĐ 1** | `GET` | `/products/search` | `PUBLIC` | Tìm kiếm sản phẩm. <br> **Query Params:** `q` (từ khóa tìm kiếm). API này sẽ trả về gợi ý sản phẩm khi người dùng đang gõ. |
| [ ] | **GĐ 1** | `GET` | `/products/:slug` | `PUBLIC` | Lấy thông tin chi tiết một sản phẩm theo slug. |
| [ ] | **GĐ 1** | `GET` | `/products/:id/related` | `PUBLIC` | Lấy danh sách sản phẩm liên quan. |
| [ ] | **GĐ 1** | `POST` | `/admin/products` | `ADMIN` | **(Admin)** Tạo sản phẩm mới. |
| [ ] | **GĐ 1** | `PUT` | `/admin/products/:id` | `ADMIN` | **(Admin)** Cập nhật thông tin sản phẩm. |
| [ ] | **GĐ 1** | `DELETE` | `/admin/products/:id` | `ADMIN` | **(Admin)** Xóa một sản phẩm. |
| [ ] | **GĐ 2** | `GET` | `/products/:id/reviews` | `PUBLIC` | Lấy tất cả đánh giá của một sản phẩm. |

---

## 6. API Giỏ hàng (Cart)

**Lưu ý:** Giỏ hàng được nhúng trong document của `user`. Các API này sẽ cập nhật `cart` object của người dùng đang đăng nhập.

| Done | Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- | :--- |
| [ ] | **GĐ 1** | `GET` | `/cart` | `USER` | Lấy thông tin giỏ hàng của người dùng. |
| [ ] | **GĐ 1** | `POST` | `/cart/items` | `USER` | Thêm một sản phẩm vào giỏ hàng. <br> **Request Body:** `{ "productId": "...", "quantity": 1 }` |
| [ ] | **GĐ 1** | `PUT` | `/cart/items/:productId` | `USER` | Cập nhật số lượng của một sản phẩm trong giỏ hàng. <br> **Request Body:** `{ "quantity": 2 }` |
| [ ] | **GĐ 1** | `DELETE` | `/cart/items/:productId` | `USER` | Xóa một sản phẩm khỏi giỏ hàng. |
| [ ] | **GĐ 1** | `DELETE` | `/cart` | `USER` | Xóa toàn bộ sản phẩm trong giỏ hàng. |

---

## 7. API Đơn hàng (Orders)

| Done | Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- | :--- |
| [ ] | **GĐ 1** | `POST` | `/orders` | `USER` | Tạo đơn hàng mới từ giỏ hàng hiện tại (Giả lập thanh toán). <br> **Request Body:** `{ "shippingAddressId": "...", "notes": "Giao giờ hành chính" }` |
| [ ] | **GĐ 1** | `GET` | `/orders` | `USER` | Lấy lịch sử mua hàng của người dùng (có phân trang). |
| [ ] | **GĐ 1** | `GET` | `/orders/:orderCode` | `USER` | Lấy chi tiết một đơn hàng của người dùng theo `orderCode`. |
| [ ] | **GĐ 1** | `PATCH` | `/orders/:orderCode/cancel` | `USER` | Người dùng tự hủy đơn hàng (nếu trạng thái là `PENDING`). |
| [ ] | **GĐ 1** | `GET` | `/admin/orders` | `ADMIN` | **(Admin)** Lấy danh sách tất cả đơn hàng (có phân trang, lọc theo status). |
| [ ] | **GĐ 1** | `GET` | `/admin/orders/:orderCode` | `ADMIN` | **(Admin)** Lấy chi tiết một đơn hàng bất kỳ. |
| [ ] | **GĐ 1** | `PATCH` | `/admin/orders/:id/status` | `ADMIN` | **(Admin)** Cập nhật trạng thái đơn hàng. <br> **Request Body:** `{ "status": "PROCESSING" }` |
| [ ] | **GĐ 2** | `POST` | `/orders/checkout/momo` | `USER` | **(Thanh toán thật)** Tạo yêu cầu thanh toán qua Momo. Trả về URL thanh toán của Momo. |

---

## 8. API Đánh giá & Phản hồi (Reviews)

| Done | Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- | :--- |
| [ ] | **GĐ 2** | `POST` | `/reviews` | `USER` | Gửi đánh giá cho một sản phẩm đã mua. <br> **Request Body:** `{ "productId": "...", "rating": 5, "comment": "Sản phẩm tốt!" }` |
| [ ] | **GĐ 2** | `PUT` | `/admin/reviews/:id/reply` | `ADMIN` | **(Admin)** Trả lời một đánh giá của người dùng. <br> **Request Body:** `{ "responseText": "Cảm ơn bạn đã tin dùng." }` |

---

## 9. API Khuyến mãi (Promotions)

| Done | Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- | :--- |
| [ ] | **GĐ 1** | N/A | N/A | N/A | **Chỉ quản lý `discountPercent`** trong API của Sản phẩm. |
| [ ] | **GĐ 2** | `GET` | `/promotions` | `PUBLIC` | Lấy danh sách các chương trình khuyến mãi đang hoạt động. |
| [ ] | **GĐ 2** | `POST` | `/admin/promotions` | `ADMIN` | **(Admin)** Tạo một chương trình khuyến mãi mới. |
| [ ] | **GĐ 2** | `PUT` | `/admin/promotions/:id` | `ADMIN` | **(Admin)** Cập nhật một chương trình khuyến mãi. |
| [ ] | **GĐ 2** | `DELETE` | `/admin/promotions/:id` | `ADMIN` | **(Admin)** Xóa một chương trình khuyến mãi. |

---

## 10. API Báo cáo & Thống kê (Reports)

| Done | Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- | :--- |
| [ ] | **GĐ 1** | `GET` | `/admin/reports/revenue` | `ADMIN` | **(Admin)** Lấy báo cáo doanh thu. <br> **Query Params:** `period` (DAILY, MONTHLY, QUARTERLY) hoặc `startDate` & `endDate`. |

---

## 11. API Lịch sử tìm kiếm (Search History) & Thông báo (Notifications)

Các API này thuộc GĐ 2 nhưng có thể được phát triển song song nếu có thời gian.

| Done | Giai đoạn | Phương thức | Endpoint | Phân quyền | Mô tả |
| :--- | :--- | :--- | :--- | :--- | :--- |
| [ ] | **GĐ 2** | `GET` | `/search-history` | `USER` | Lấy lịch sử các từ khóa tìm kiếm gần đây của người dùng. |
| [ ] | **GĐ 2** | `GET` | `/notifications` | `USER` | Lấy danh sách thông báo của người dùng. |
| [ ] | **GĐ 2** | `POST` | `/notifications/:id/read` | `USER` | Đánh dấu một thông báo là đã đọc. |

---

