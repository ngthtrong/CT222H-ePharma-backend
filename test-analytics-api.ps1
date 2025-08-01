# Test Analytics APIs Script
Write-Host "=== TESTING ANALYTICS APIs ===" -ForegroundColor Green

# Step 1: Login to get JWT token
Write-Host "`n1. Testing Login API..." -ForegroundColor Yellow
$loginData = @{
    email = "admin1@admin.com"
    password = "admin2"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/auth/login" -Method POST -Body $loginData -ContentType "application/json"
    $token = $loginResponse.data.accessToken
    Write-Host "✅ Login SUCCESS - Token obtained" -ForegroundColor Green
} catch {
    Write-Host "❌ Login FAILED: $($_.Exception.Message)" -ForegroundColor Red
    exit
}

# Headers for authenticated requests
$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# Step 2: Test Real-time Analytics API
Write-Host "`n2. Testing Real-time Analytics API..." -ForegroundColor Yellow
try {
    $realtimeResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/admin/analytics/realtime" -Method GET -Headers $headers
    Write-Host "✅ Real-time Analytics SUCCESS" -ForegroundColor Green
    Write-Host "   - Orders Last 24h: $($realtimeResponse.data.ordersLast24h)"
    Write-Host "   - Active Users: $($realtimeResponse.data.activeUsersOnline)"
    Write-Host "   - Peak Hour: $($realtimeResponse.data.peakHour)"
} catch {
    Write-Host "❌ Real-time Analytics FAILED: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 3: Test Advanced Dashboard API
Write-Host "`n3. Testing Advanced Dashboard API..." -ForegroundColor Yellow
try {
    $dashboardResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/admin/analytics/dashboard?startDate=2025-01-01&endDate=2025-07-31" -Method GET -Headers $headers
    Write-Host "✅ Advanced Dashboard SUCCESS" -ForegroundColor Green
    Write-Host "   - Total Revenue: $($dashboardResponse.data.totalRevenue)"
    Write-Host "   - Total Orders: $($dashboardResponse.data.totalOrders)"
    Write-Host "   - Revenue Growth Rate: $($dashboardResponse.data.revenueGrowthRate)%"
} catch {
    Write-Host "❌ Advanced Dashboard FAILED: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 4: Test Excel Export API
Write-Host "`n4. Testing Excel Export API..." -ForegroundColor Yellow
try {
    $exportResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/v1/admin/reports/revenue/export/excel?startDate=2025-01-01&endDate=2025-07-31" -Method GET -Headers $headers
    Write-Host "✅ Excel Export SUCCESS" -ForegroundColor Green
    Write-Host "   - Status Code: $($exportResponse.StatusCode)"
    Write-Host "   - Content Type: $($exportResponse.Headers.'Content-Type')"
    Write-Host "   - File Size: $($exportResponse.RawContentLength) bytes"
} catch {
    Write-Host "❌ Excel Export FAILED: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 5: Test WebSocket endpoint (basic connectivity)
Write-Host "`n5. Testing WebSocket Endpoint..." -ForegroundColor Yellow
try {
    $wsResponse = Invoke-WebRequest -Uri "http://localhost:8081/ws" -Method GET -Headers $headers
    Write-Host "✅ WebSocket Endpoint accessible" -ForegroundColor Green
} catch {
    if ($_.Exception.Message -like "*404*") {
        Write-Host "❌ WebSocket Endpoint not found" -ForegroundColor Red
    } else {
        Write-Host "ℹ️ WebSocket requires proper connection (expected behavior)" -ForegroundColor Cyan
    }
}

Write-Host "`n=== TEST COMPLETED ===" -ForegroundColor Green
