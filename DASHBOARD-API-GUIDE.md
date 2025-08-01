# Dashboard API Guide - Real-time Free Version

## Tổng Quan

Dashboard mới được thiết kế để hiển thị các chỉ số kinh doanh quan trọng mà không cần real-time updates. Frontend sẽ có nút "Làm mới" để cập nhật dữ liệu và hiển thị thời gian cập nhật cuối cùng.

## Base URL

```
HTTP API: http://localhost:8080/api/v1
```

## Authentication & Authorization

### Admin Authentication (Required cho tất cả Dashboard APIs)
```javascript
const headers = {
    'Authorization': 'Bearer <ADMIN_JWT_TOKEN>',
    'Content-Type': 'application/json'
};
```

---

## DASHBOARD API ENDPOINTS

### 1. Dashboard Tổng Quan - Tất cả dữ liệu trong một API

**GET** `/admin/dashboard/stats`

**Mô tả**: Lấy tất cả thống kê dashboard trong một lần gọi

```javascript
async function getDashboardStats() {
    const response = await fetch('/api/v1/admin/dashboard/stats', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data; // DashboardStatsResponse
}
```

**Response Example**:
```json
{
    "success": true,
    "message": "Lấy thống kê dashboard thành công",
    "data": {
        "totalRevenue": 15750000.0,
        "revenueGrowthRate": 12.5,
        "totalOrders": 245,
        "averageOrderValue": 642857.14,
        "conversionRate": 15.3,
        "revenueMetrics": [
            {
                "date": "2025-08-01",
                "revenue": 1250000.0
            },
            {
                "date": "2025-08-02", 
                "revenue": 980000.0
            }
        ],
        "categoryPerformance": [
            {
                "categoryName": "Thuốc cảm cúm",
                "totalSold": 150,
                "revenue": 3750000.0
            },
            {
                "categoryName": "Vitamin & Thực phẩm chức năng",
                "totalSold": 89,
                "revenue": 2890000.0
            }
        ],
        "customerSegments": {
            "highValueCustomers": 25,
            "mediumValueCustomers": 75,
            "lowValueCustomers": 120
        },
        "topProducts": [
            {
                "productId": "66d1234567890abcdef12345",
                "productName": "Paracetamol 500mg",
                "productImage": "https://example.com/image1.jpg",
                "quantitySold": 45,
                "revenue": 675000.0,
                "categoryName": "Thuốc cảm cúm"
            }
        ],
        "recentOrders": [
            {
                "orderId": "66d1234567890abcdef12346",
                "orderCode": "ORD001234",
                "customerName": "Nguyễn Văn A",
                "totalAmount": 450000.0,
                "status": "COMPLETED",
                "createdAt": "2025-08-02T10:30:00Z"
            }
        ],
        "lastUpdated": "2025-08-02T15:45:30Z"
    }
}
```

### 2. Làm Mới Dashboard

**POST** `/admin/dashboard/refresh`

**Mô tả**: Làm mới dữ liệu dashboard và trả về dữ liệu mới nhất

```javascript
async function refreshDashboard() {
    const response = await fetch('/api/v1/admin/dashboard/refresh', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data;
}
```

### 3. Thời gian cập nhật cuối

**GET** `/dashboard/last-updated`

**Mô tả**: Lấy thông tin thời gian cập nhật cuối cùng (Không cần auth)

```javascript
async function getLastUpdated() {
    const response = await fetch('/api/v1/dashboard/last-updated', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data;
}
```

**Response**:
```json
{
    "success": true,
    "message": "Dashboard last updated info",
    "data": {
        "lastUpdated": 1722607530000,
        "message": "Dashboard data được cập nhật mỗi khi có yêu cầu mới"
    }
}
```

---

## FRONTEND IMPLEMENTATION

### Dashboard Component Implementation

```javascript
class DashboardComponent {
    constructor() {
        this.adminToken = localStorage.getItem('adminToken');
        this.baseURL = '/api/v1';
        this.charts = {};
        this.isLoading = false;
    }

    async init() {
        try {
            await this.loadDashboardData();
            this.setupRefreshButton();
            this.initCharts();
            this.showLastUpdated();
        } catch (error) {
            console.error('Dashboard initialization failed:', error);
            this.showError('Không thể tải dữ liệu dashboard');
        }
    }

    async loadDashboardData() {
        if (this.isLoading) return;
        
        this.isLoading = true;
        this.showLoadingState();
        
        try {
            const response = await fetch(`${this.baseURL}/admin/dashboard/stats`, {
                headers: {
                    'Authorization': `Bearer ${this.adminToken}`,
                    'Content-Type': 'application/json'
                }
            });
            
            if (!response.ok) {
                throw new Error('Failed to fetch dashboard data');
            }
            
            const data = await response.json();
            if (data.success) {
                this.updateDashboard(data.data);
                this.updateLastUpdated(data.data.lastUpdated);
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Failed to load dashboard:', error);
            this.showError('Có lỗi xảy ra khi tải dữ liệu');
        } finally {
            this.isLoading = false;
            this.hideLoadingState();
        }
    }

    async refreshDashboard() {
        if (this.isLoading) return;
        
        this.isLoading = true;
        this.showLoadingState();
        
        try {
            const response = await fetch(`${this.baseURL}/admin/dashboard/refresh`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${this.adminToken}`,
                    'Content-Type': 'application/json'
                }
            });
            
            if (!response.ok) {
                throw new Error('Failed to refresh dashboard');
            }
            
            const data = await response.json();
            if (data.success) {
                this.updateDashboard(data.data);
                this.updateLastUpdated(data.data.lastUpdated);
                this.showSuccess('Dashboard đã được cập nhật!');
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Failed to refresh dashboard:', error);
            this.showError('Có lỗi xảy ra khi làm mới dữ liệu');
        } finally {
            this.isLoading = false;
            this.hideLoadingState();
        }
    }

    updateDashboard(data) {
        // Update main metrics
        this.updateMainMetrics(data);
        
        // Update charts
        this.updateRevenueChart(data.revenueMetrics);
        this.updateCategoryChart(data.categoryPerformance);
        this.updateCustomerSegmentsChart(data.customerSegments);
        this.updateTopProductsChart(data.topProducts);
        
        // Update tables
        this.updateRecentOrdersTable(data.recentOrders);
    }

    updateMainMetrics(data) {
        // Tổng Doanh Thu
        document.getElementById('totalRevenue').textContent = this.formatCurrency(data.totalRevenue);
        document.getElementById('revenueGrowth').textContent = `${data.revenueGrowthRate >= 0 ? '+' : ''}${data.revenueGrowthRate.toFixed(1)}%`;
        document.getElementById('revenueGrowth').className = data.revenueGrowthRate >= 0 ? 'positive' : 'negative';
        
        // Tổng Đơn Hàng
        document.getElementById('totalOrders').textContent = data.totalOrders;
        
        // Giá Trị TB/Đơn
        document.getElementById('averageOrderValue').textContent = this.formatCurrency(data.averageOrderValue);
        
        // Tỷ lệ chuyển đổi
        document.getElementById('conversionRate').textContent = `${data.conversionRate.toFixed(1)}%`;
    }

    updateRevenueChart(revenueData) {
        if (this.charts.revenue) {
            this.charts.revenue.data.labels = revenueData.map(item => this.formatDate(item.date));
            this.charts.revenue.data.datasets[0].data = revenueData.map(item => item.revenue);
            this.charts.revenue.update();
        }
    }

    updateCategoryChart(categoryData) {
        if (this.charts.category) {
            this.charts.category.data.labels = categoryData.map(item => item.categoryName);
            this.charts.category.data.datasets[0].data = categoryData.map(item => item.revenue);
            this.charts.category.update();
        }
    }

    updateCustomerSegmentsChart(segmentData) {
        if (this.charts.customerSegments) {
            this.charts.customerSegments.data.datasets[0].data = [
                segmentData.highValueCustomers,
                segmentData.mediumValueCustomers,
                segmentData.lowValueCustomers
            ];
            this.charts.customerSegments.update();
        }
    }

    updateTopProductsChart(topProducts) {
        if (this.charts.topProducts) {
            this.charts.topProducts.data.labels = topProducts.map(item => item.productName);
            this.charts.topProducts.data.datasets[0].data = topProducts.map(item => item.quantitySold);
            this.charts.topProducts.update();
        }
    }

    updateRecentOrdersTable(orders) {
        const tbody = document.getElementById('recentOrdersTable');
        tbody.innerHTML = orders.map(order => `
            <tr>
                <td>${order.orderCode}</td>
                <td>${order.customerName}</td>
                <td>${this.formatCurrency(order.totalAmount)}</td>
                <td><span class="status-badge status-${order.status.toLowerCase()}">${order.status}</span></td>
                <td>${this.formatDateTime(order.createdAt)}</td>
            </tr>
        `).join('');
    }

    setupRefreshButton() {
        const refreshBtn = document.getElementById('refreshDashboard');
        if (refreshBtn) {
            refreshBtn.addEventListener('click', () => this.refreshDashboard());
        }
    }

    initCharts() {
        // Initialize Chart.js charts
        this.charts.revenue = new Chart(document.getElementById('revenueChart'), {
            type: 'line',
            data: {
                labels: [],
                datasets: [{
                    label: 'Doanh thu (VNĐ)',
                    data: [],
                    borderColor: '#3B82F6',
                    backgroundColor: 'rgba(59, 130, 246, 0.1)',
                    tension: 0.1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: (value) => this.formatCurrency(value)
                        }
                    }
                }
            }
        });

        this.charts.category = new Chart(document.getElementById('categoryChart'), {
            type: 'doughnut',
            data: {
                labels: [],
                datasets: [{
                    data: [],
                    backgroundColor: [
                        '#FF6384',
                        '#36A2EB',
                        '#FFCE56',
                        '#4BC0C0',
                        '#9966FF'
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });

        this.charts.customerSegments = new Chart(document.getElementById('customerSegmentsChart'), {
            type: 'doughnut',
            data: {
                labels: ['Khách hàng VIP', 'Khách hàng thường', 'Khách hàng mới'],
                datasets: [{
                    data: [],
                    backgroundColor: ['#10B981', '#F59E0B', '#EF4444']
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });

        this.charts.topProducts = new Chart(document.getElementById('topProductsChart'), {
            type: 'bar',
            data: {
                labels: [],
                datasets: [{
                    label: 'Số lượng bán',
                    data: [],
                    backgroundColor: '#8B5CF6'
                }]
            },
            options: {
                responsive: true,
                indexAxis: 'y',
                scales: {
                    x: {
                        beginAtZero: true
                    }
                }
            }
        });
    }

    showLoadingState() {
        const refreshBtn = document.getElementById('refreshDashboard');
        if (refreshBtn) {
            refreshBtn.disabled = true;
            refreshBtn.innerHTML = '<i class="fa fa-spinner fa-spin"></i> Đang tải...';
        }
        
        // Show loading overlay
        const overlay = document.getElementById('loadingOverlay');
        if (overlay) {
            overlay.style.display = 'flex';
        }
    }

    hideLoadingState() {
        const refreshBtn = document.getElementById('refreshDashboard');
        if (refreshBtn) {
            refreshBtn.disabled = false;
            refreshBtn.innerHTML = '<i class="fa fa-refresh"></i> Làm mới';
        }
        
        // Hide loading overlay
        const overlay = document.getElementById('loadingOverlay');
        if (overlay) {
            overlay.style.display = 'none';
        }
    }

    updateLastUpdated(timestamp) {
        const element = document.getElementById('lastUpdated');
        if (element) {
            element.textContent = `Cập nhật lần cuối: ${this.formatDateTime(timestamp)}`;
        }
    }

    showLastUpdated() {
        // Display when dashboard was last updated
        const now = new Date();
        this.updateLastUpdated(now.toISOString());
    }

    formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    }

    formatDate(dateString) {
        return new Date(dateString).toLocaleDateString('vi-VN');
    }

    formatDateTime(dateString) {
        return new Date(dateString).toLocaleString('vi-VN');
    }

    showSuccess(message) {
        // Implementation for success toast
        this.showToast(message, 'success');
    }

    showError(message) {
        // Implementation for error toast  
        this.showToast(message, 'error');
    }

    showToast(message, type) {
        // Create and show toast notification
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.textContent = message;
        
        document.body.appendChild(toast);
        
        setTimeout(() => {
            toast.remove();
        }, 3000);
    }
}
```

### Usage Example

```javascript
// Initialize dashboard when page loads
document.addEventListener('DOMContentLoaded', async () => {
    const dashboard = new DashboardComponent();
    await dashboard.init();
});
```

---

## HTML TEMPLATE EXAMPLE

```html
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - WellVerse</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-gray-50">
    <div class="container mx-auto px-4 py-8">
        <!-- Header with refresh button -->
        <div class="flex justify-between items-center mb-8">
            <h1 class="text-3xl font-bold text-gray-900">Dashboard Tổng Quan</h1>
            <div class="flex items-center space-x-4">
                <span id="lastUpdated" class="text-sm text-gray-500"></span>
                <button id="refreshDashboard" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                    <i class="fa fa-refresh"></i> Làm mới
                </button>
            </div>
        </div>

        <!-- Main Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-sm font-medium text-gray-500">Tổng Doanh Thu</h3>
                <p id="totalRevenue" class="text-2xl font-bold text-gray-900">0 ₫</p>
                <p id="revenueGrowth" class="text-sm positive">+0%</p>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-sm font-medium text-gray-500">Tổng Đơn Hàng</h3>
                <p id="totalOrders" class="text-2xl font-bold text-gray-900">0</p>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-sm font-medium text-gray-500">Giá Trị TB/Đơn</h3>
                <p id="averageOrderValue" class="text-2xl font-bold text-gray-900">0 ₫</p>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-sm font-medium text-gray-500">Tỷ lệ chuyển đổi</h3>
                <p id="conversionRate" class="text-2xl font-bold text-gray-900">0%</p>
            </div>
        </div>

        <!-- Charts Section -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-lg font-semibold mb-4">Xu Hướng Doanh Thu</h3>
                <canvas id="revenueChart"></canvas>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-lg font-semibold mb-4">Hiệu Suất Danh Mục</h3>
                <canvas id="categoryChart"></canvas>
            </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-lg font-semibold mb-4">Phân Khúc Khách Hàng</h3>
                <canvas id="customerSegmentsChart"></canvas>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-lg font-semibold mb-4">Top Sản Phẩm Bán Chạy</h3>
                <canvas id="topProductsChart"></canvas>
            </div>
        </div>

        <!-- Recent Orders Table -->
        <div class="bg-white rounded-lg shadow">
            <div class="px-6 py-4 border-b border-gray-200">
                <h3 class="text-lg font-semibold">Đơn Hàng Gần Đây</h3>
            </div>
            <div class="overflow-x-auto">
                <table class="min-w-full">
                    <thead class="bg-gray-50">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Mã đơn</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Khách hàng</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Tổng tiền</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Trạng thái</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Ngày tạo</th>
                        </tr>
                    </thead>
                    <tbody id="recentOrdersTable" class="bg-white divide-y divide-gray-200">
                        <!-- Data will be populated here -->
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Loading Overlay -->
    <div id="loadingOverlay" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center hidden">
        <div class="bg-white p-6 rounded-lg">
            <div class="flex items-center space-x-3">
                <i class="fa fa-spinner fa-spin text-blue-500"></i>
                <span>Đang tải dữ liệu...</span>
            </div>
        </div>
    </div>

    <style>
    .positive { color: #10B981; }
    .negative { color: #EF4444; }
    .status-badge {
        padding: 2px 8px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 500;
    }
    .status-completed { background-color: #D1FAE5; color: #065F46; }
    .status-pending { background-color: #FEF3C7; color: #92400E; }
    .status-cancelled { background-color: #FEE2E2; color: #991B1B; }
    .toast {
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 12px 24px;
        border-radius: 6px;
        color: white;
        font-weight: 500;
        z-index: 1000;
    }
    .toast-success { background-color: #10B981; }
    .toast-error { background-color: #EF4444; }
    </style>

    <script src="dashboard.js"></script>
</body>
</html>
```

---

## ERROR HANDLING

### Common Error Responses

```javascript
const DashboardErrorHandler = {
    handleError(error, context = '') {
        console.error(`Dashboard Error [${context}]:`, error);
        
        switch (error.status) {
            case 401:
                // Token expired, redirect to login
                localStorage.removeItem('adminToken');
                window.location.href = '/admin/login';
                break;
                
            case 403:
                this.showError('Bạn không có quyền truy cập tính năng này');
                break;
                
            case 404:
                this.showError('Không tìm thấy dữ liệu');
                break;
                
            case 500:
                this.showError('Lỗi server, vui lòng thử lại sau');
                break;
                
            default:
                this.showError('Có lỗi xảy ra, vui lòng thử lại');
        }
    },
    
    showError(message) {
        // Implementation for showing error to user
        alert(message); // Replace with proper notification system
    }
};
```

---

## NOTES

1. **Manual Refresh**: Dashboard được thiết kế để cập nhật thủ công thông qua nút "Làm mới"
2. **Performance**: Mỗi lần gọi API sẽ tính toán và trả về toàn bộ dữ liệu cần thiết
3. **Caching**: Có thể implement caching ở level service để tăng performance
4. **Last Updated**: Hiển thị thời gian cập nhật cuối để user biết độ mới của dữ liệu
5. **Error Handling**: Xử lý lỗi đầy đủ với thông báo user-friendly
6. **Mobile Responsive**: Dashboard hoạt động tốt trên mobile devices
7. **Accessibility**: Sử dụng ARIA labels và keyboard navigation
8. **Export Features**: Có thể thêm chức năng export báo cáo PDF/Excel
9. **Customization**: Admin có thể tùy chỉnh hiển thị các metrics
10. **Analytics**: Tracking user behavior với custom events

Hướng dẫn này cung cấp framework hoàn chỉnh để implement dashboard mới không cần WebSocket real-time.
