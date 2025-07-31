# Hướng Dẫn Sử Dụng Report API cho Frontend

## Tổng Quan

API Report cung cấp các báo cáo thống kê và phân tích dữ liệu kinh doanh dành cho **Admin**. Hệ thống hỗ trợ các loại báo cáo: doanh thu, hiệu suất sản phẩm, thống kê đơn hàng và phân tích người dùng.

- **Admin Only**: Tất cả endpoint đều yêu cầu quyền Admin
- **Date Range**: Hỗ trợ lọc theo khoảng thời gian
- **Multiple Report Types**: Báo cáo doanh thu, sản phẩm, đơn hàng, người dùng

## Base URL

```
http://localhost:8080/api/v1
```

## Authentication & Authorization

### Admin Authentication (Required)
```javascript
const headers = {
    'Authorization': 'Bearer <ADMIN_JWT_TOKEN>',
    'Content-Type': 'application/json'
};
```

## Report Types & Parameters

### Date Range Parameters
- `startDate`: Ngày bắt đầu (định dạng: `yyyy-MM-dd`)
- `endDate`: Ngày kết thúc (định dạng: `yyyy-MM-dd`)
- `reportType`: Loại báo cáo (`DAILY`, `WEEKLY`, `MONTHLY`, `YEARLY`)

---

## ADMIN API ENDPOINTS

### 1. Báo Cáo Doanh Thu

**GET** `/admin/reports/revenue`

**Mô tả**: Lấy báo cáo doanh thu theo khoảng thời gian và loại báo cáo

**Query Parameters**:
- `startDate` (required): Ngày bắt đầu
- `endDate` (required): Ngày kết thúc  
- `reportType` (optional): Loại báo cáo (mặc định: `MONTHLY`)

```javascript
async function getRevenueReport(startDate, endDate, reportType = 'MONTHLY') {
    const params = new URLSearchParams({
        startDate: startDate, // 'YYYY-MM-DD'
        endDate: endDate,     // 'YYYY-MM-DD'
        reportType: reportType
    });

    const response = await fetch(`/api/v1/admin/reports/revenue?${params}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data; // RevenueReportResponse
}
```

**Response Example**:
```json
{
    "success": true,
    "message": "Lấy báo cáo doanh thu thành công",
    "data": {
        "reportType": "MONTHLY",
        "periodStart": "2025-01-01T00:00:00Z",
        "periodEnd": "2025-01-31T23:59:59Z",
        "totalRevenue": 15750000.0,
        "totalOrders": 45,
        "topProducts": [
            {
                "productId": "prod_001",
                "productName": "Vitamin C 1000mg",
                "quantitySold": 120,
                "revenue": 3600000.0
            },
            {
                "productId": "prod_002", 
                "productName": "Omega-3 Fish Oil",
                "quantitySold": 85,
                "revenue": 2550000.0
            }
        ],
        "generatedAt": "2025-01-31T10:30:00Z"
    }
}
```

### 2. Báo Cáo Hiệu Suất Sản Phẩm

**GET** `/admin/reports/products`

**Mô tả**: Lấy báo cáo hiệu suất bán hàng của các sản phẩm

**Query Parameters**:
- `startDate` (required): Ngày bắt đầu
- `endDate` (required): Ngày kết thúc

```javascript
async function getProductPerformanceReport(startDate, endDate) {
    const params = new URLSearchParams({
        startDate: startDate,
        endDate: endDate
    });

    const response = await fetch(`/api/v1/admin/reports/products?${params}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data; // Array of ProductPerformanceResponse
}
```

**Response Example**:
```json
{
    "success": true,
    "message": "Lấy báo cáo hiệu suất sản phẩm thành công",
    "data": [
        {
            "productId": "prod_001",
            "productName": "Vitamin C 1000mg",
            "quantitySold": 120,
            "revenue": 3600000.0
        },
        {
            "productId": "prod_002",
            "productName": "Omega-3 Fish Oil", 
            "quantitySold": 85,
            "revenue": 2550000.0
        },
        {
            "productId": "prod_003",
            "productName": "Probiotics",
            "quantitySold": 65,
            "revenue": 1950000.0
        }
    ]
}
```

### 3. Báo Cáo Thống Kê Đơn Hàng

**GET** `/admin/reports/orders`

**Mô tả**: Lấy thống kê chi tiết về đơn hàng

**Query Parameters**:
- `startDate` (required): Ngày bắt đầu
- `endDate` (required): Ngày kết thúc

```javascript
async function getOrderStatistics(startDate, endDate) {
    const params = new URLSearchParams({
        startDate: startDate,
        endDate: endDate
    });

    const response = await fetch(`/api/v1/admin/reports/orders?${params}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data; // OrderStatisticsResponse
}
```

**Response Example**:
```json
{
    "success": true,
    "message": "Lấy báo cáo thống kê đơn hàng thành công",
    "data": {
        "periodStart": "2025-01-01T00:00:00Z",
        "periodEnd": "2025-01-31T23:59:59Z",
        "totalOrders": 45,
        "completedOrders": 38,
        "cancelledOrders": 3,
        "pendingOrders": 4,
        "averageOrderValue": 350000.0,
        "generatedAt": "2025-01-31T10:30:00Z"
    }
}
```

### 4. Báo Cáo Phân Tích Người Dùng

**GET** `/admin/reports/users`

**Mô tả**: Lấy thống kê và phân tích về người dùng

**Query Parameters**:
- `startDate` (required): Ngày bắt đầu
- `endDate` (required): Ngày kết thúc

```javascript
async function getUserAnalytics(startDate, endDate) {
    const params = new URLSearchParams({
        startDate: startDate,
        endDate: endDate
    });

    const response = await fetch(`/api/v1/admin/reports/users?${params}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${adminToken}`,
            'Content-Type': 'application/json'
        }
    });
    
    const data = await response.json();
    return data.data; // UserAnalyticsResponse
}
```

**Response Example**:
```json
{
    "success": true,
    "message": "Lấy báo cáo phân tích người dùng thành công",
    "data": {
        "periodStart": "2025-01-01T00:00:00Z",
        "periodEnd": "2025-01-31T23:59:59Z",
        "totalUsers": 1250,
        "newUsers": 85,
        "activeUsers": 320,
        "generatedAt": "2025-01-31T10:30:00Z"
    }
}
```

---

## Data Models

### RevenueReportResponse
```javascript
{
    reportType: string,         // Loại báo cáo (DAILY, WEEKLY, MONTHLY, YEARLY)
    periodStart: string,        // Ngày bắt đầu kỳ báo cáo
    periodEnd: string,          // Ngày kết thúc kỳ báo cáo
    totalRevenue: number,       // Tổng doanh thu
    totalOrders: number,        // Tổng số đơn hàng
    topProducts: [              // Top sản phẩm bán chạy
        {
            productId: string,
            productName: string,
            quantitySold: number,
            revenue: number
        }
    ],
    generatedAt: string         // Thời gian tạo báo cáo
}
```

### ProductPerformanceResponse
```javascript
{
    productId: string,          // ID sản phẩm
    productName: string,        // Tên sản phẩm
    quantitySold: number,       // Số lượng đã bán
    revenue: number             // Doanh thu từ sản phẩm
}
```

### OrderStatisticsResponse
```javascript
{
    periodStart: string,        // Ngày bắt đầu
    periodEnd: string,          // Ngày kết thúc
    totalOrders: number,        // Tổng số đơn hàng
    completedOrders: number,    // Số đơn hoàn thành
    cancelledOrders: number,    // Số đơn bị hủy
    pendingOrders: number,      // Số đơn đang chờ
    averageOrderValue: number,  // Giá trị đơn hàng trung bình
    generatedAt: string         // Thời gian tạo báo cáo
}
```

### UserAnalyticsResponse
```javascript
{
    periodStart: string,        // Ngày bắt đầu
    periodEnd: string,          // Ngày kết thúc
    totalUsers: number,         // Tổng số người dùng
    newUsers: number,           // Số người dùng mới
    activeUsers: number,        // Số người dùng hoạt động
    generatedAt: string         // Thời gian tạo báo cáo
}
```

---

## Error Handling

### Common Error Responses

**400 Bad Request** - Khoảng thời gian không hợp lệ:
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

**403 Forbidden** - Không có quyền Admin:
```json
{
    "success": false,
    "message": "Bạn không có quyền truy cập tính năng này",
    "data": null
}
```

**500 Internal Server Error** - Lỗi tạo báo cáo:
```json
{
    "success": false,
    "message": "Lỗi khi tạo báo cáo",
    "data": null
}
```

---

## Frontend Integration Examples

### React Hook cho Report Management

```javascript
import { useState } from 'react';

export const useReports = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const generateReport = async (reportType, startDate, endDate, options = {}) => {
        setLoading(true);
        setError(null);
        
        try {
            let data;
            switch(reportType) {
                case 'revenue':
                    data = await getRevenueReport(startDate, endDate, options.reportType);
                    break;
                case 'products':
                    data = await getProductPerformanceReport(startDate, endDate);
                    break;
                case 'orders':
                    data = await getOrderStatistics(startDate, endDate);
                    break;
                case 'users':
                    data = await getUserAnalytics(startDate, endDate);
                    break;
                default:
                    throw new Error('Loại báo cáo không hợp lệ');
            }
            return data;
        } catch (err) {
            setError(err.message);
            throw err;
        } finally {
            setLoading(false);
        }
    };

    return {
        generateReport,
        loading,
        error
    };
};
```

### Report Dashboard Component

```javascript
import { useState, useEffect } from 'react';
import { Line, Bar, Pie } from 'react-chartjs-2';

const ReportDashboard = () => {
    const [dateRange, setDateRange] = useState({
        startDate: '2025-01-01',
        endDate: '2025-01-31'
    });
    const [reportData, setReportData] = useState({
        revenue: null,
        products: null,
        orders: null,
        users: null
    });
    const { generateReport, loading } = useReports();

    const loadAllReports = async () => {
        try {
            const [revenue, products, orders, users] = await Promise.all([
                generateReport('revenue', dateRange.startDate, dateRange.endDate),
                generateReport('products', dateRange.startDate, dateRange.endDate),
                generateReport('orders', dateRange.startDate, dateRange.endDate),
                generateReport('users', dateRange.startDate, dateRange.endDate)
            ]);

            setReportData({ revenue, products, orders, users });
        } catch (error) {
            console.error('Failed to load reports:', error);
        }
    };

    useEffect(() => {
        loadAllReports();
    }, [dateRange]);

    const handleDateChange = (field, value) => {
        setDateRange(prev => ({ ...prev, [field]: value }));
    };

    return (
        <div className="report-dashboard">
            <div className="dashboard-header">
                <h1>Báo Cáo Kinh Doanh</h1>
                
                <div className="date-filter">
                    <input
                        type="date"
                        value={dateRange.startDate}
                        onChange={(e) => handleDateChange('startDate', e.target.value)}
                    />
                    <span>đến</span>
                    <input
                        type="date"
                        value={dateRange.endDate}
                        onChange={(e) => handleDateChange('endDate', e.target.value)}
                    />
                    <button onClick={loadAllReports} disabled={loading}>
                        {loading ? 'Đang tải...' : 'Cập nhật'}
                    </button>
                </div>
            </div>

            {loading && <div className="loading">Đang tải báo cáo...</div>}

            <div className="report-grid">
                {/* Revenue Summary */}
                {reportData.revenue && (
                    <div className="report-card">
                        <h3>Tổng Quan Doanh Thu</h3>
                        <div className="revenue-summary">
                            <div className="metric">
                                <span className="value">
                                    {reportData.revenue.totalRevenue.toLocaleString()} VNĐ
                                </span>
                                <span className="label">Tổng Doanh Thu</span>
                            </div>
                            <div className="metric">
                                <span className="value">{reportData.revenue.totalOrders}</span>
                                <span className="label">Tổng Đơn Hàng</span>
                            </div>
                        </div>
                    </div>
                )}

                {/* Order Statistics */}
                {reportData.orders && (
                    <div className="report-card">
                        <h3>Thống Kê Đơn Hàng</h3>
                        <Pie
                            data={{
                                labels: ['Hoàn thành', 'Đang chờ', 'Đã hủy'],
                                datasets: [{
                                    data: [
                                        reportData.orders.completedOrders,
                                        reportData.orders.pendingOrders,
                                        reportData.orders.cancelledOrders
                                    ],
                                    backgroundColor: ['#10B981', '#F59E0B', '#EF4444']
                                }]
                            }}
                        />
                        <div className="order-metrics">
                            <div>Giá trị TB: {reportData.orders.averageOrderValue.toLocaleString()} VNĐ</div>
                        </div>
                    </div>
                )}

                {/* Top Products */}
                {reportData.products && (
                    <div className="report-card">
                        <h3>Top Sản Phẩm Bán Chạy</h3>
                        <Bar
                            data={{
                                labels: reportData.products.slice(0, 10).map(p => p.productName),
                                datasets: [{
                                    label: 'Số lượng bán',
                                    data: reportData.products.slice(0, 10).map(p => p.quantitySold),
                                    backgroundColor: '#3B82F6'
                                }]
                            }}
                            options={{
                                responsive: true,
                                scales: {
                                    y: {
                                        beginAtZero: true
                                    }
                                }
                            }}
                        />
                    </div>
                )}

                {/* User Analytics */}
                {reportData.users && (
                    <div className="report-card">
                        <h3>Phân Tích Người Dùng</h3>
                        <div className="user-metrics">
                            <div className="metric">
                                <span className="value">{reportData.users.totalUsers}</span>
                                <span className="label">Tổng Người Dùng</span>
                            </div>
                            <div className="metric">
                                <span className="value">{reportData.users.newUsers}</span>
                                <span className="label">Người Dùng Mới</span>
                            </div>
                            <div className="metric">
                                <span className="value">{reportData.users.activeUsers}</span>
                                <span className="label">Người Dùng Hoạt Động</span>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};
```

### Export Report Functionality

```javascript
const ExportReports = ({ reportData, dateRange }) => {
    const exportToCSV = (data, filename) => {
        let csv = '';
        
        if (Array.isArray(data)) {
            // For product performance data
            if (data.length > 0 && data[0].productId) {
                csv = 'Mã Sản Phẩm,Tên Sản Phẩm,Số Lượng Bán,Doanh Thu\n';
                data.forEach(item => {
                    csv += `${item.productId},${item.productName},${item.quantitySold},${item.revenue}\n`;
                });
            }
        } else {
            // For other report types
            csv = Object.keys(data).map(key => `${key},${data[key]}`).join('\n');
        }

        const blob = new Blob([csv], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
    };

    const exportToPDF = async (reportData) => {
        // Integration with PDF library like jsPDF
        const { jsPDF } = await import('jspdf');
        const doc = new jsPDF();
        
        doc.text('Báo Cáo Kinh Doanh', 20, 20);
        doc.text(`Từ ${dateRange.startDate} đến ${dateRange.endDate}`, 20, 30);
        
        if (reportData.revenue) {
            doc.text(`Tổng Doanh Thu: ${reportData.revenue.totalRevenue.toLocaleString()} VNĐ`, 20, 50);
            doc.text(`Tổng Đơn Hàng: ${reportData.revenue.totalOrders}`, 20, 60);
        }
        
        doc.save('bao-cao-kinh-doanh.pdf');
    };

    return (
        <div className="export-controls">
            <button onClick={() => exportToCSV(reportData.products, 'san-pham-ban-chay.csv')}>
                Xuất CSV Sản Phẩm
            </button>
            <button onClick={() => exportToPDF(reportData)}>
                Xuất PDF Báo Cáo
            </button>
        </div>
    );
};
```

### Scheduled Reports Hook

```javascript
const useScheduledReports = () => {
    const [schedules, setSchedules] = useState([]);

    const createSchedule = (reportConfig) => {
        const schedule = {
            id: Date.now(),
            type: reportConfig.type,
            frequency: reportConfig.frequency, // daily, weekly, monthly
            recipients: reportConfig.recipients,
            lastRun: null,
            nextRun: calculateNextRun(reportConfig.frequency)
        };
        
        setSchedules(prev => [...prev, schedule]);
        
        // In real app, this would call backend API
        console.log('Created scheduled report:', schedule);
    };

    const calculateNextRun = (frequency) => {
        const now = new Date();
        switch(frequency) {
            case 'daily':
                return new Date(now.getTime() + 24 * 60 * 60 * 1000);
            case 'weekly':
                return new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000);
            case 'monthly':
                const nextMonth = new Date(now.getFullYear(), now.getMonth() + 1, now.getDate());
                return nextMonth;
            default:
                return null;
        }
    };

    return {
        schedules,
        createSchedule
    };
};
```

---

## Performance Optimization

### Caching Strategy

```javascript
const useReportCache = () => {
    const [cache, setCache] = useState(new Map());

    const getCacheKey = (reportType, startDate, endDate, options = {}) => {
        return `${reportType}-${startDate}-${endDate}-${JSON.stringify(options)}`;
    };

    const getCachedReport = (reportType, startDate, endDate, options) => {
        const key = getCacheKey(reportType, startDate, endDate, options);
        const cached = cache.get(key);
        
        if (cached && Date.now() - cached.timestamp < 300000) { // 5 minutes
            return cached.data;
        }
        
        return null;
    };

    const setCachedReport = (reportType, startDate, endDate, data, options) => {
        const key = getCacheKey(reportType, startDate, endDate, options);
        setCache(prev => new Map(prev.set(key, {
            data,
            timestamp: Date.now()
        })));
    };

    return {
        getCachedReport,
        setCachedReport
    };
};
```

---

## Notes

1. **Date Validation**: Luôn validate `startDate < endDate` trước khi gọi API
2. **Large Data**: Với khoảng thời gian lớn, báo cáo có thể mất thời gian load
3. **Real-time**: Dữ liệu báo cáo không real-time, thường delay 15-30 phút
4. **Export Limits**: Giới hạn export tối đa 10,000 records mỗi lần
5. **Caching**: Nên cache báo cáo để tránh tạo lại quá nhiều lần
6. **Permissions**: Chỉ Admin mới có quyền truy cập tất cả báo cáo
