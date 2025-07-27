# Tổng quan Kiến trúc Backend

Tài liệu này phác thảo kiến trúc kỹ thuật của backend dự án WellVerse.

## 1. Công nghệ sử dụng

* **Framework:** Spring Boot (Java)
* **Cơ sở dữ liệu:** MongoDB (quản lý qua MongoDB Atlas)
* **Bảo mật:** Spring Security với JSON Web Tokens (JWT)
* **Đặc tả API:** OpenAPI (trước đây là Swagger), được cấu hình trong `OpenApiConfig.java`.
* **Công cụ xây dựng:** Maven
* **Ngôn ngữ:** Tiếng Việt và Tiếng Anh

## 2. Kiến trúc Hệ thống

Backend tuân theo kiến trúc ba tầng cổ điển được xây dựng trên mô hình RESTful API.

`Client (ReactJS) <---> RESTful API (Spring Boot Server) <---> Cơ sở dữ liệu (MongoDB)`

### Các nguyên tắc kiến trúc chính:

* **Phi trạng thái (Statelessness):** API là phi trạng thái. Xác thực client được quản lý thông qua JWT được gửi trong header `Authorization`.
* **Phân tách mối quan tâm (Separation of Concerns):** Backend hoàn toàn tách biệt với frontend, chỉ giao tiếp qua REST API.
* **Kiến trúc phân lớp (Layered Architecture):** Ứng dụng Spring Boot được cấu trúc thành các lớp riêng biệt:
  * **Lớp Controller (`controller`):** Xử lý các yêu cầu HTTP đến, xác thực đầu vào (sử dụng DTO) và ủy quyền logic nghiệp vụ cho lớp service.
  * **Lớp Service (`service`):** Chứa logic nghiệp vụ cốt lõi của ứng dụng. Nó điều phối việc truy cập và thao tác dữ liệu bằng cách tương tác với các repository.
  * **Lớp Repository (`repository`):** Quản lý việc truy cập và lưu trữ dữ liệu bằng Spring Data MongoDB. Nó định nghĩa các interface kế thừa `MongoRepository`.
  * **Lớp Model (`model`):** Định nghĩa các đối tượng miền (thực thể) ánh xạ trực tiếp đến các collection trong MongoDB.

## 3. Thiết kế Cơ sở dữ liệu

* **Cơ sở dữ liệu:** MongoDB
* **Lược đồ (Schema):** Lược đồ cơ sở dữ liệu được định nghĩa trong `database.md`. Các collection chính bao gồm `users`, `products`, `categories`, `orders`, và `carts`.
* **Quyết định thiết kế quan trọng:** Collection `carts` được tách biệt một cách có chủ ý khỏi collection `users`. Điều này cho phép hệ thống quản lý giỏ hàng cho cả người dùng đã xác thực (trường `userId`) và người dùng khách ẩn danh (trường `anonymousSessionId`), đây là một yêu cầu tính năng cốt lõi.

## 4. Xác thực và Phân quyền

* **Xác thực (Authentication):**
  * Endpoint `POST /api/v1/auth/login` xác thực thông tin đăng nhập của người dùng.
  * Khi đăng nhập thành công, một `accessToken` JWT được tạo và trả về cho client.
  * Các yêu cầu đã xác thực sau đó phải bao gồm token trong header `Authorization: Bearer <token>`.
* **Phân quyền (Authorization):**
  * Được triển khai bằng Spring Security.
  * Quyền truy cập vào các endpoint được hạn chế dựa trên vai trò của người dùng (`USER`, `ADMIN`).
  * Các quy tắc dựa trên vai trò được định nghĩa trong lớp `SecurityConfig` và sử dụng các annotation trên các phương thức của controller.

## 5. Cấu trúc API

* **Kiểu:** RESTful
* **Đường dẫn cơ sở (Base Path):** `/api/v1`
* **Định dạng dữ liệu:** JSON cho tất cả các body của request và response.
* **Phiên bản (Versioning):** API được đánh phiên bản thông qua tiền tố URL (`/v1`).
* **Hỗ trợ người dùng khách:** Header tùy chỉnh `X-Cart-Session-ID` được sử dụng để xác định và quản lý giỏ hàng cho người dùng khách chưa xác thực.

## 6. Cấu trúc mã nguồn dự án

Logic ứng dụng chính nằm trong `src/main/java/ct222h/vegeta/projectbackend/`.

* `config/`: Các lớp cấu hình Spring (Security, JWT, MongoDB, OpenAPI).
* `constants/`: Các hằng số toàn ứng dụng.
* `controller/`: Các trình xử lý endpoint API.
* `dto/`: Các Đối tượng Truyền dữ liệu (Data Transfer Objects) để xác thực và định hình request/response.
* `exception/`: Các lớp exception tùy chỉnh và một trình xử lý exception toàn cục.
* `model/`: Các model document của MongoDB.
* `repository/`: Các interface repository của Spring Data MongoDB.
* `security/`: Logic tạo/xác thực JWT và service chi tiết người dùng.
* `service/`: Triển khai logic nghiệp vụ.
