# Hướng Dẫn Sử Dụng Review API cho Frontend

## Tổng Quan

API Review hỗ trợ hệ thống đánh giá sản phẩm cho cả **User** và **Admin**. User có thể xem và tạo đánh giá sản phẩm, Admin có thể quản lý tất cả đánh giá và trả lời đánh giá.

- **Public**: Xem đánh giá sản phẩm (không cần đăng nhập)
- **User**: Tạo đánh giá cho sản phẩm đã mua
- **Admin**: Quản lý tất cả đánh giá, trả lời đánh giá, xóa đánh giá

## Base URL

```
http://localhost:8080/api/v1
```

## Authentication & Authorization

### Public Access (Xem đánh giá)
```javascript
// Không cần authentication cho việc xem đánh giá
const headers = {
    'Content-Type': 'application/json'
};
```

### User Authentication
```javascript
const headers = {
    'Authorization': 'Bearer <JWT_TOKEN>',
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

## Review Rating System

### Hệ Thống Đánh Giá
- `1 sao`: Rất không hài lòng
- `2 sao`: Không hài lòng  
- `3 sao`: Bình thường
- `4 sao`: Hài lòng
- `5 sao`: Rất hài lòng

---

## PUBLIC API ENDPOINTS

### 1. Lấy Đánh Giá Của Sản Phẩm

**GET** `/products/{id}/reviews`

**Mô tả**: Lấy tất cả đánh giá của một sản phẩm cụ thể (không cần đăng nhập)

```javascript
async function getProductReviews(productId) {
    const response = await fetch(`/api/v1/products/${productId}/reviews`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data; // Array of ReviewResponse
}
```

**Response Example**:
```json
{
    "success": true,
    "message": "Lấy danh sách đánh giá thành công",
    "data": [
        {
            "id": "review_001",
            "productId": "prod_001",
            "productName": "Vitamin C 1000mg",
            "userId": "user_123",
            "userName": "Nguyễn Văn A",
            "rating": 5,
            "comment": "Sản phẩm rất tốt, tôi đã sử dụng 1 tháng và cảm thấy sức khỏe cải thiện rõ rệt.",
            "adminReply": {
                "responseText": "Cảm ơn bạn đã tin tưởng sản phẩm của chúng tôi!",
                "repliedAt": "2025-01-31T15:30:00Z"
            },
            "createdAt": "2025-01-30T10:30:00Z"
        },
        {
            "id": "review_002",
            "productId": "prod_001",
            "productName": "Vitamin C 1000mg",
            "userId": "user_456",
            "userName": "Trần Thị B",
            "rating": 4,
            "comment": "Chất lượng ổn, giá cả hợp lý.",
            "adminReply": null,
            "createdAt": "2025-01-29T14:20:00Z"
        }
    ]
}
```

---

## USER API ENDPOINTS

### 1. Tạo Đánh Giá Sản Phẩm

**POST** `/reviews`

**Mô tả**: User tạo đánh giá cho sản phẩm (chỉ có thể đánh giá sản phẩm đã mua)

**Request Body**:
```json
{
    "productId": "prod_001",
    "rating": 5,
    "comment": "Sản phẩm chất lượng tuyệt vời, tôi sẽ mua lại lần nữa!"
}
```

```javascript
async function createReview(reviewData) {
    const response = await fetch('/api/v1/reviews', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${userToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reviewData)
    });
    
    const data = await response.json();
    return data.data; // ReviewResponse object
}
```

**Validation Rules**:
- `productId`: Bắt buộc, phải là ID sản phẩm hợp lệ
- `rating`: Bắt buộc, từ 1-5 sao
- `comment`: Tùy chọn, tối đa 1000 ký tự

---

## ADMIN API ENDPOINTS

### 1. Lấy Tất Cả Đánh Giá (Admin)

**GET** `/admin/reviews`

**Mô tả**: Admin lấy tất cả đánh giá trong hệ thống

```javascript
async function getAllReviews() {
    const response = await fetch('/api/v1/admin/reviews', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data; // Array of ReviewResponse
}
```

### 2. Trả Lời Đánh Giá

**PUT** `/admin/reviews/{id}/reply`

**Mô tả**: Admin trả lời đánh giá của khách hàng

**Request Body**:
```json
{
    "responseText": "Cảm ơn bạn đã đánh giá sản phẩm. Chúng tôi rất vui vì bạn hài lòng với chất lượng sản phẩm!"
}
```

```javascript
async function replyToReview(reviewId, responseText) {
    const response = await fetch(`/api/v1/admin/reviews/${reviewId}/reply`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ responseText })
    });
    
    const data = await response.json();
    return data.data; // Updated ReviewResponse
}
```

**Validation Rules**:
- `responseText`: Bắt buộc, tối đa 1000 ký tự

### 3. Xóa Đánh Giá

**DELETE** `/admin/reviews/{id}`

**Mô tả**: Admin xóa đánh giá không phù hợp

```javascript
async function deleteReview(reviewId) {
    const response = await fetch(`/api/v1/admin/reviews/${reviewId}`, {
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

### ReviewRequest
```javascript
{
    productId: string,  // Bắt buộc - ID sản phẩm
    rating: number,     // Bắt buộc - Đánh giá từ 1-5 sao
    comment: string     // Tùy chọn - Bình luận (max 1000 chars)
}
```

### ReviewReplyRequest
```javascript
{
    responseText: string  // Bắt buộc - Nội dung trả lời (max 1000 chars)
}
```

### ReviewResponse
```javascript
{
    id: string,           // ID đánh giá
    productId: string,    // ID sản phẩm
    productName: string,  // Tên sản phẩm
    userId: string,       // ID người đánh giá
    userName: string,     // Tên người đánh giá
    rating: number,       // Số sao đánh giá (1-5)
    comment: string,      // Bình luận
    adminReply: {         // Phản hồi của admin (có thể null)
        responseText: string,
        repliedAt: string
    },
    createdAt: string     // Thời gian tạo đánh giá
}
```

---

## Error Handling

### Common Error Responses

**404 Not Found** - Không tìm thấy đánh giá:
```json
{
    "success": false,
    "message": "Không tìm thấy đánh giá",
    "data": null
}
```

**400 Bad Request** - Dữ liệu không hợp lệ:
```json
{
    "success": false,
    "message": "Đánh giá phải từ 1 đến 5 sao",
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

**403 Forbidden** - Không có quyền:
```json
{
    "success": false,
    "message": "Bạn chỉ có thể đánh giá sản phẩm đã mua",
    "data": null
}
```

---

## Frontend Integration Examples

### React Hook cho Review Management

```javascript
import { useState, useEffect } from 'react';

export const useProductReviews = (productId) => {
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [stats, setStats] = useState({
        averageRating: 0,
        totalReviews: 0,
        ratingDistribution: {}
    });

    const fetchReviews = async () => {
        setLoading(true);
        try {
            const data = await getProductReviews(productId);
            setReviews(data);
            calculateStats(data);
            setError(null);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const calculateStats = (reviewsData) => {
        const total = reviewsData.length;
        const sum = reviewsData.reduce((acc, review) => acc + review.rating, 0);
        const average = total > 0 ? (sum / total).toFixed(1) : 0;
        
        const distribution = reviewsData.reduce((acc, review) => {
            acc[review.rating] = (acc[review.rating] || 0) + 1;
            return acc;
        }, {});

        setStats({
            averageRating: parseFloat(average),
            totalReviews: total,
            ratingDistribution: distribution
        });
    };

    const addReview = async (reviewData) => {
        try {
            const newReview = await createReview(reviewData);
            setReviews(prev => [newReview, ...prev]);
            calculateStats([newReview, ...reviews]);
            return newReview;
        } catch (err) {
            setError(err.message);
            throw err;
        }
    };

    useEffect(() => {
        if (productId) {
            fetchReviews();
        }
    }, [productId]);

    return {
        reviews,
        stats,
        loading,
        error,
        addReview,
        refresh: fetchReviews
    };
};
```

### Star Rating Component

```javascript
const StarRating = ({ rating, onRatingChange, readonly = false, size = 'medium' }) => {
    const [hoverRating, setHoverRating] = useState(0);

    const getSizeClass = () => {
        switch(size) {
            case 'small': return 'text-sm';
            case 'large': return 'text-2xl';
            default: return 'text-lg';
        }
    };

    return (
        <div className={`flex items-center space-x-1 ${getSizeClass()}`}>
            {[1, 2, 3, 4, 5].map((star) => (
                <button
                    key={star}
                    type="button"
                    disabled={readonly}
                    className={`${
                        star <= (hoverRating || rating)
                            ? 'text-yellow-400'
                            : 'text-gray-300'
                    } ${readonly ? 'cursor-default' : 'cursor-pointer hover:text-yellow-500'}`}
                    onClick={() => !readonly && onRatingChange?.(star)}
                    onMouseEnter={() => !readonly && setHoverRating(star)}
                    onMouseLeave={() => !readonly && setHoverRating(0)}
                >
                    ⭐
                </button>
            ))}
        </div>
    );
};
```

### Review Form Component

```javascript
const ReviewForm = ({ productId, onReviewSubmitted }) => {
    const [formData, setFormData] = useState({
        rating: 0,
        comment: ''
    });
    const [submitting, setSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (formData.rating === 0) {
            alert('Vui lòng chọn số sao đánh giá');
            return;
        }

        setSubmitting(true);
        try {
            const reviewData = {
                productId,
                rating: formData.rating,
                comment: formData.comment.trim()
            };

            await createReview(reviewData);
            setFormData({ rating: 0, comment: '' });
            onReviewSubmitted?.();
            alert('Đánh giá đã được gửi thành công!');
        } catch (error) {
            alert('Gửi đánh giá thất bại: ' + error.message);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="review-form">
            <div className="form-group">
                <label>Đánh giá của bạn:</label>
                <StarRating
                    rating={formData.rating}
                    onRatingChange={(rating) => setFormData({...formData, rating})}
                />
            </div>

            <div className="form-group">
                <label>Nhận xét (tùy chọn):</label>
                <textarea
                    value={formData.comment}
                    onChange={(e) => setFormData({...formData, comment: e.target.value})}
                    placeholder="Chia sẻ trải nghiệm của bạn về sản phẩm..."
                    maxLength={1000}
                    rows={4}
                />
                <small>{formData.comment.length}/1000</small>
            </div>

            <button 
                type="submit" 
                disabled={submitting || formData.rating === 0}
                className="submit-btn"
            >
                {submitting ? 'Đang gửi...' : 'Gửi đánh giá'}
            </button>
        </form>
    );
};
```

### Review List Component

```javascript
const ReviewList = ({ productId }) => {
    const { reviews, stats, loading, error } = useProductReviews(productId);

    if (loading) return <div>Đang tải đánh giá...</div>;
    if (error) return <div>Lỗi: {error}</div>;

    return (
        <div className="review-section">
            {/* Stats Summary */}
            <div className="review-stats">
                <div className="average-rating">
                    <span className="rating-number">{stats.averageRating}</span>
                    <StarRating rating={stats.averageRating} readonly size="large" />
                    <span className="total-reviews">({stats.totalReviews} đánh giá)</span>
                </div>

                {/* Rating Distribution */}
                <div className="rating-distribution">
                    {[5, 4, 3, 2, 1].map(star => (
                        <div key={star} className="rating-bar">
                            <span>{star} sao</span>
                            <div className="bar">
                                <div 
                                    className="bar-fill"
                                    style={{
                                        width: `${(stats.ratingDistribution[star] || 0) / stats.totalReviews * 100}%`
                                    }}
                                />
                            </div>
                            <span>{stats.ratingDistribution[star] || 0}</span>
                        </div>
                    ))}
                </div>
            </div>

            {/* Review List */}
            <div className="reviews-list">
                {reviews.map(review => (
                    <div key={review.id} className="review-item">
                        <div className="review-header">
                            <div className="reviewer-info">
                                <span className="reviewer-name">{review.userName}</span>
                                <StarRating rating={review.rating} readonly />
                            </div>
                            <span className="review-date">
                                {new Date(review.createdAt).toLocaleDateString()}
                            </span>
                        </div>

                        {review.comment && (
                            <div className="review-comment">
                                {review.comment}
                            </div>
                        )}

                        {review.adminReply && (
                            <div className="admin-reply">
                                <div className="reply-header">
                                    <span className="reply-label">Phản hồi từ shop:</span>
                                    <span className="reply-date">
                                        {new Date(review.adminReply.repliedAt).toLocaleDateString()}
                                    </span>
                                </div>
                                <div className="reply-content">
                                    {review.adminReply.responseText}
                                </div>
                            </div>
                        )}
                    </div>
                ))}
            </div>

            {reviews.length === 0 && (
                <div className="no-reviews">
                    Chưa có đánh giá nào cho sản phẩm này
                </div>
            )}
        </div>
    );
};
```

### Admin Review Management

```javascript
const AdminReviewManager = () => {
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(false);
    const [replyText, setReplyText] = useState({});

    const fetchAllReviews = async () => {
        setLoading(true);
        try {
            const data = await getAllReviews();
            setReviews(data);
        } catch (error) {
            console.error('Failed to fetch reviews:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleReply = async (reviewId) => {
        const text = replyText[reviewId];
        if (!text?.trim()) return;

        try {
            await replyToReview(reviewId, text.trim());
            setReplyText(prev => ({ ...prev, [reviewId]: '' }));
            fetchAllReviews(); // Refresh
            alert('Trả lời thành công!');
        } catch (error) {
            alert('Trả lời thất bại: ' + error.message);
        }
    };

    const handleDelete = async (reviewId) => {
        if (!confirm('Bạn có chắc muốn xóa đánh giá này?')) return;

        try {
            await deleteReview(reviewId);
            setReviews(prev => prev.filter(r => r.id !== reviewId));
            alert('Xóa thành công!');
        } catch (error) {
            alert('Xóa thất bại: ' + error.message);
        }
    };

    useEffect(() => {
        fetchAllReviews();
    }, []);

    return (
        <div className="admin-review-manager">
            <h2>Quản lý đánh giá</h2>
            
            {reviews.map(review => (
                <div key={review.id} className="admin-review-item">
                    <div className="review-info">
                        <h4>{review.productName}</h4>
                        <p>Người đánh giá: {review.userName}</p>
                        <StarRating rating={review.rating} readonly />
                        <p>{review.comment}</p>
                        <small>{new Date(review.createdAt).toLocaleString()}</small>
                    </div>

                    {review.adminReply ? (
                        <div className="existing-reply">
                            <strong>Đã trả lời:</strong>
                            <p>{review.adminReply.responseText}</p>
                            <small>{new Date(review.adminReply.repliedAt).toLocaleString()}</small>
                        </div>
                    ) : (
                        <div className="reply-form">
                            <textarea
                                value={replyText[review.id] || ''}
                                onChange={(e) => setReplyText(prev => ({
                                    ...prev,
                                    [review.id]: e.target.value
                                }))}
                                placeholder="Nhập phản hồi..."
                                maxLength={1000}
                            />
                            <button onClick={() => handleReply(review.id)}>
                                Trả lời
                            </button>
                        </div>
                    )}

                    <div className="admin-actions">
                        <button 
                            onClick={() => handleDelete(review.id)}
                            className="delete-btn"
                        >
                            Xóa đánh giá
                        </button>
                    </div>
                </div>
            ))}
        </div>
    );
};
```

---

## Notes

1. **Permission Check**: User chỉ có thể đánh giá sản phẩm đã mua
2. **One Review per Product**: Mỗi user chỉ được đánh giá 1 lần cho 1 sản phẩm
3. **Moderation**: Admin nên kiểm duyệt đánh giá trước khi hiển thị public
4. **Rich Text**: Có thể hỗ trợ rich text cho bình luận chi tiết hơn
5. **Image Upload**: Có thể cho phép upload hình ảnh kèm đánh giá
6. **Helpful Votes**: Có thể thêm tính năng vote "helpful" cho đánh giá
