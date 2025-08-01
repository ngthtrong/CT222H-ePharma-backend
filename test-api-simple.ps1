# Test Analytics APIs Script
Write-Host "=== TESTING ANALYTICS APIs ==="

# Step 1: Login to get JWT token
Write-Host "1. Testing Login API..."
$loginData = @{
    email = "admin1@admin.com"
    password = "admin2"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/auth/login" -Method POST -Body $loginData -ContentType "application/json"
    $token = $loginResponse.data.accessToken
    Write-Host "SUCCESS: Login - Token obtained"
} catch {
    Write-Host "FAILED: Login - $($_.Exception.Message)"
    exit
}

# Headers for authenticated requests
$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# Step 2: Test Real-time Analytics API
Write-Host "2. Testing Real-time Analytics API..."
try {
    $realtimeResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/admin/analytics/realtime" -Method GET -Headers $headers
    Write-Host "SUCCESS: Real-time Analytics"
    Write-Host "   - Orders Last 24h: $($realtimeResponse.data.ordersLast24h)"
    Write-Host "   - Active Users: $($realtimeResponse.data.activeUsersOnline)"
    Write-Host "   - Peak Hour: $($realtimeResponse.data.peakHour)"
} catch {
    Write-Host "FAILED: Real-time Analytics - $($_.Exception.Message)"
}

# Step 3: Test Advanced Dashboard API
Write-Host "3. Testing Advanced Dashboard API..."
try {
    $dashboardResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/admin/analytics/dashboard?startDate=2025-01-01&endDate=2025-07-31" -Method GET -Headers $headers
    Write-Host "SUCCESS: Advanced Dashboard"
    Write-Host "   - Total Revenue: $($dashboardResponse.data.totalRevenue)"
    Write-Host "   - Total Orders: $($dashboardResponse.data.totalOrders)"
    Write-Host "   - Revenue Growth Rate: $($dashboardResponse.data.revenueGrowthRate)%"
} catch {
    Write-Host "FAILED: Advanced Dashboard - $($_.Exception.Message)"
}

# Step 4: Test Excel Export API
Write-Host "4. Testing Excel Export API..."
try {
    $exportResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/v1/admin/reports/revenue/export/excel?startDate=2025-01-01&endDate=2025-07-31" -Method GET -Headers $headers
    Write-Host "SUCCESS: Excel Export"
    Write-Host "   - Status Code: $($exportResponse.StatusCode)"
    Write-Host "   - File Size: $($exportResponse.RawContentLength) bytes"
} catch {
    Write-Host "FAILED: Excel Export - $($_.Exception.Message)"
}

Write-Host "=== TEST COMPLETED ==="
