# Hướng Dẫn Sử Dụng Promotion API cho Frontend

## Tổng Quan

API Promotion hỗ trợ quản lý các chương trình khuyến mãi từ việc xem, tạo, cập nhật đến xóa cho cả **User** và **Admin**. Hệ thống sử dụng JWT token để xác thực và phân quyền.

- **User**: Có thể xem các khuyến mãi đang hoạt động
- **Admin**: Có thể quản lý tất cả khuyến mãi, tạo, cập nhật và xóa khuyến mãi

## Base URL

```
http://localhost:8080/api/v1
```

## Authentication & Authorization

### User Authentication (Public Access)
```javascript
// Không cần authentication cho các endpoint public
const headers = {
    'Content-Type': 'application/json'
};
```

### Admin Authentication
```javascript
const headers = {
    'Authorization': 'Bearer <ADMIN_JWT_TOKEN>',
    'Content-Type': 'application/json'
};
```

## Promotion Status

### Trạng Thái Khuyến Mãi
- `isActive: true`: Khuyến mãi được kích hoạt
- `isActive: false`: Khuyến mãi bị tạm dừng
- `isCurrentlyActive: true`: Khuyến mãi đang trong thời gian hiệu lực
- `isCurrentlyActive: false`: Khuyến mãi ngoài thời gian hiệu lực

### Notification Types
- `ORDER`: Thông báo liên quan đến đơn hàng
- `PRODUCT`: Thông báo liên quan đến sản phẩm
- `PROMOTION`: Thông báo khuyến mãi
- `SYSTEM`: Thông báo hệ thống

---

## PUBLIC API ENDPOINTS

### 1. Lấy Danh Sách Khuyến Mãi Đang Hoạt Động

**GET** `/promotions`

**Mô tả**: Lấy tất cả khuyến mãi đang hoạt động và trong thời gian hiệu lực

```javascript
async function getActivePromotions() {
    const response = await fetch('/api/v1/promotions', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data; // Array of PromotionResponse
}
```

**Response Example**:
```json
{
    "success": true,
    "message": "Lấy danh sách khuyến mãi thành công",
    "data": [
        {
            "id": "promo_001",
            "name": "Flash Sale Cuối Tuần",
            "description": "Giảm giá 50% tất cả sản phẩm vitamin và thực phẩm chức năng",
            "bannerImageUrl": "https://example.com/banner1.jpg",
            "startDate": "2025-01-31T00:00:00Z",
            "endDate": "2025-02-02T23:59:59Z",
            "isActive": true,
            "applicableProductIds": ["prod_001", "prod_002", "prod_003"],
            "promoImageOverlay": "https://example.com/overlay1.png",
            "promoIcon": "https://example.com/icon1.svg",
            "isCurrentlyActive": true
        }
    ]
}
```

---

## ADMIN API ENDPOINTS

### 1. Lấy Tất Cả Khuyến Mãi (Admin)

**GET** `/admin/promotions`

**Mô tả**: Admin lấy tất cả khuyến mãi (bao gồm cả đã hết hạn và chưa kích hoạt)

```javascript
async function getAllPromotions() {
    const response = await fetch('/api/v1/admin/promotions', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data; // Array of PromotionResponse
}
```

### 2. Lấy Chi Tiết Khuyến Mãi Theo ID

**GET** `/admin/promotions/{id}`

**Mô tả**: Admin lấy chi tiết khuyến mãi cụ thể

```javascript
async function getPromotionById(promotionId) {
    const response = await fetch(`/api/v1/admin/promotions/${promotionId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data; // PromotionResponse object
}
```

### 3. Tạo Khuyến Mãi Mới

**POST** `/admin/promotions`

**Mô tả**: Admin tạo khuyến mãi mới

**Request Body**:
```json
{
    "name": "Khuyến Mãi Tết 2025",
    "description": "Giảm giá đặc biệt nhân dịp Tết Nguyên Đán",
    "bannerImageUrl": "https://example.com/tet2025-banner.jpg",
    "startDate": "2025-02-01T00:00:00Z",
    "endDate": "2025-02-15T23:59:59Z",
    "isActive": true,
    "applicableProductIds": ["prod_001", "prod_002"],
    "promoImageOverlay": "https://example.com/tet-overlay.png",
    "promoIcon": "https://example.com/tet-icon.svg"
}
```

```javascript
async function createPromotion(promotionData) {
    const response = await fetch('/api/v1/admin/promotions', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(promotionData)
    });
    
    const data = await response.json();
    return data.data; // PromotionResponse object
}
```

**Validation Rules**:
- `name`: Bắt buộc, không được để trống
- `startDate`: Bắt buộc, định dạng ISO 8601
- `endDate`: Bắt buộc, phải sau `startDate`
- `description`: Tùy chọn
- `bannerImageUrl`: Tùy chọn, URL hình ảnh banner
- `isActive`: Mặc định `true`
- `applicableProductIds`: Tùy chọn, danh sách ID sản phẩm áp dụng
- `promoImageOverlay`: Tùy chọn, URL overlay hiển thị trên sản phẩm
- `promoIcon`: Tùy chọn, URL icon khuyến mãi

### 4. Cập Nhật Khuyến Mãi

**PUT** `/admin/promotions/{id}`

**Mô tả**: Admin cập nhật thông tin khuyến mãi

**Request Body**: Tương tự như tạo khuyến mãi

```javascript
async function updatePromotion(promotionId, updateData) {
    const response = await fetch(`/api/v1/admin/promotions/${promotionId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updateData)
    });
    
    const data = await response.json();
    return data.data; // PromotionResponse object
}
```

### 5. Xóa Khuyến Mãi

**DELETE** `/admin/promotions/{id}`

**Mô tả**: Admin xóa khuyến mãi

```javascript
async function deletePromotion(promotionId) {
    const response = await fetch(`/api/v1/admin/promotions/${promotionId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        }
    });
    
    return response.ok;
}
```

---

## Data Models

### PromotionRequest
```javascript
{
    name: string,               // Bắt buộc - Tên khuyến mãi
    description: string,        // Tùy chọn - Mô tả khuyến mãi
    bannerImageUrl: string,     // Tùy chọn - URL banner
    startDate: string,          // Bắt buộc - Ngày bắt đầu (ISO 8601)
    endDate: string,            // Bắt buộc - Ngày kết thúc (ISO 8601)
    isActive: boolean,          // Tùy chọn - Trạng thái kích hoạt (mặc định true)
    applicableProductIds: [],   // Tùy chọn - Danh sách ID sản phẩm áp dụng
    promoImageOverlay: string,  // Tùy chọn - URL overlay image
    promoIcon: string          // Tùy chọn - URL icon
}
```

### PromotionResponse
```javascript
{
    id: string,                 // ID khuyến mãi
    name: string,               // Tên khuyến mãi
    description: string,        // Mô tả khuyến mãi
    bannerImageUrl: string,     // URL banner
    startDate: string,          // Ngày bắt đầu
    endDate: string,            // Ngày kết thúc
    isActive: boolean,          // Trạng thái kích hoạt
    applicableProductIds: [],   // Danh sách ID sản phẩm áp dụng
    promoImageOverlay: string,  // URL overlay image
    promoIcon: string,         // URL icon
    isCurrentlyActive: boolean  // Khuyến mãi có đang trong thời gian hiệu lực không
}
```

---

## Error Handling

### Common Error Responses

**404 Not Found** - Không tìm thấy khuyến mãi:
```json
{
    "success": false,
    "message": "Không tìm thấy khuyến mãi",
    "data": null
}
```

**400 Bad Request** - Dữ liệu không hợp lệ:
```json
{
    "success": false,
    "message": "Ngày kết thúc phải sau ngày bắt đầu",
    "data": null
}
```

**401 Unauthorized** - Chưa đăng nhập:
```json
{
    "success": false,
    "message": "Token không hợp lệ",
    "data": null
}
```

**403 Forbidden** - Không có quyền truy cập:
```json
{
    "success": false,
    "message": "Bạn không có quyền truy cập tính năng này",
    "data": null
}
```

---

## Frontend Integration Examples

### React Hook cho Promotion Management

```javascript
import { useState, useEffect } from 'react';

export const usePromotions = () => {
    const [promotions, setPromotions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchActivePromotions = async () => {
        setLoading(true);
        try {
            const data = await getActivePromotions();
            setPromotions(data);
            setError(null);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const createPromotion = async (promotionData) => {
        try {
            const newPromotion = await createPromotion(promotionData);
            setPromotions(prev => [...prev, newPromotion]);
            return newPromotion;
        } catch (err) {
            setError(err.message);
            throw err;
        }
    };

    useEffect(() => {
        fetchActivePromotions();
    }, []);

    return {
        promotions,
        loading,
        error,
        fetchActivePromotions,
        createPromotion
    };
};
```

### Hiển thị Promotion Badge trên Product

```javascript
const ProductCard = ({ product, promotions }) => {
    const activePromotion = promotions.find(promo => 
        promo.isCurrentlyActive && 
        promo.applicableProductIds?.includes(product.id)
    );

    return (
        <div className="product-card">
            {activePromotion && (
                <div className="promotion-badge">
                    {activePromotion.promoIcon && (
                        <img src={activePromotion.promoIcon} alt="Promo" />
                    )}
                    <span>{activePromotion.name}</span>
                </div>
            )}
            <img src={product.imageUrl} alt={product.name} />
            {activePromotion?.promoImageOverlay && (
                <div className="promo-overlay">
                    <img src={activePromotion.promoImageOverlay} alt="Sale" />
                </div>
            )}
            <h3>{product.name}</h3>
            <p>{product.price}</p>
        </div>
    );
};
```

---

## Notes

1. **Date Format**: Tất cả dates sử dụng định dạng ISO 8601 (UTC)
2. **Image URLs**: Nên validate URLs trước khi submit
3. **Product IDs**: Đảm bảo `applicableProductIds` chứa các ID sản phẩm hợp lệ
4. **Cache Strategy**: Nên cache danh sách khuyến mãi đang hoạt động để tối ưu performance
5. **Real-time Updates**: Consider implementing WebSocket cho real-time promotion updates
