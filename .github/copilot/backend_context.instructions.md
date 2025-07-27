# Bối cảnh Phát triển Backend cho Dự án WellVerse

Tài liệu này cung cấp bối cảnh cần thiết để phát triển và tái cấu trúc (refactor) backend của nền tảng thương mại điện tử WellVerse.

## 1. Mục tiêu & Trạng thái Dự án

*   **Mục tiêu:** Xây dựng backend cho trang thương mại điện tử "WellVerse" chuyên về dược phẩm và các sản phẩm sức khỏe.
*   **Trạng thái hiện tại:**
    *   Backend đã có các triển khai ban đầu cho các API **Người dùng, Danh mục, và Sản phẩm**.
    *   **QUAN TRỌNG:** Mã nguồn hiện có này được dựa trên một **thiết kế đã lỗi thời**, trong đó giỏ hàng của người dùng được nhúng bên trong collection `users`.
    *   Dự án hiện đã áp dụng một **kiến trúc mới**, trong đó giỏ hàng được lưu trữ trong một collection `carts` riêng biệt để hỗ trợ cả người dùng đã đăng ký và khách mua hàng vãng lai.
    *   **Tất cả các tài liệu thiết kế (`database.md`, `api-description.md`) đã được cập nhật để phản ánh kiến trúc mới này.**

## 2. Nhiệm vụ Cốt lõi: Tái cấu trúc và Triển khai API Giỏ hàng & Đơn hàng

Nhiệm vụ chính của backend là **tái cấu trúc mã nguồn hiện có** để phù hợp với thiết kế mới và triển khai đầy đủ chức năng quản lý giỏ hàng và đơn hàng.

### Yêu cầu Tái cấu trúc Chính:

1.  **Tách Giỏ hàng khỏi Người dùng:**
    *   Loại bỏ mọi trường và logic liên quan đến giỏ hàng khỏi `User` model, repository, service, và controller.
    *   Thực thể `User` sẽ không còn trường `cart` được nhúng nữa.

2.  **Triển khai Module `Cart`:**
    *   Tạo mới `Cart` model, `CartRepository`, `CartService`, và `CartController`.
    *   `Cart` model phải có một `userId` tùy chọn (cho người dùng đã đăng ký) và một `anonymousSessionId` tùy chọn (cho người dùng khách).
    *   `CartController` phải xử lý các yêu cầu cho cả hai loại người dùng bằng cách kiểm tra header `Authorization` (cho JWT) hoặc header tùy chỉnh `X-Cart-Session-ID`.

3.  **Triển khai Tính năng "Gộp Giỏ hàng" (Merge Cart):**
    *   Một endpoint mới và quan trọng, `POST /api/v1/cart/merge`, phải được tạo.
    *   Endpoint này được gọi sau khi một người dùng có giỏ hàng khách đăng nhập.
    *   Nó sẽ gộp các mặt hàng từ giỏ hàng khách (được xác định bởi `X-Cart-Session-ID`) vào giỏ hàng hiện có của người dùng (hoặc tạo mới nếu chưa có) và sau đó xóa giỏ hàng khách.

4.  **Triển khai Module `Order`:**
    *   Tạo module `Order` (`model`, `repository`, `service`, `controller`).
    *   Endpoint `POST /orders` sẽ tạo một đơn hàng từ giỏ hàng hiện tại của người dùng, sao chép các chi tiết cần thiết về sản phẩm và địa chỉ vào tài liệu đơn hàng, sau đó làm trống giỏ hàng của người dùng.

## 3. Các Tệp Quan trọng để Tham khảo

*   **`database.md`**: **Nguồn thông tin chính xác nhất** cho tất cả các lược đồ collection của MongoDB. Hãy chú ý kỹ đến cấu trúc của các collection `carts` và `orders`.
*   **`api-description.md`**: **Nguồn thông tin chính xác nhất** cho tất cả các endpoint REST API, bao gồm đường dẫn, phương thức, các header bắt buộc (`Authorization`, `X-Cart-Session-ID`), và cấu trúc request/response.
*   **`demo_json_for_api.md`**: Cung cấp các ví dụ JSON cụ thể cho các request và response API, rất hữu ích cho việc xây dựng DTO và kiểm thử.

## 4. Hướng dẫn Kỹ thuật

*   **Framework:** Spring Boot
*   **Truy cập Cơ sở dữ liệu:** Sử dụng Spring Data MongoDB bằng cách tạo các interface repository kế thừa `MongoRepository`.
*   **Xác thực:** Sử dụng Spring Security với JWT. Sẽ cần một `JwtTokenProvider` (hoặc tương tự) và một triển khai `UserDetailsService`.
*   **Xác thực dữ liệu (Validation):** Sử dụng các Đối tượng Truyền dữ liệu (DTO) ở lớp controller với các annotation xác thực (`@Valid`, `@NotBlank`, v.v.) để xác thực các body của request đến.
*   **Xử lý Ngoại lệ (Exception Handling):** Sử dụng một lớp `@ControllerAdvice` để xử lý các ngoại lệ một cách toàn cục và trả về các phản hồi lỗi JSON nhất quán.
