package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.DashboardResponse;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import ct222h.vegeta.projectbackend.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final DashboardService dashboardService;
    
    @Autowired
    private AuthorizationService authorizationService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // PUBLIC ENDPOINT for health check
    @GetMapping("/dashboard/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard service is running", "OK"));
    }

    // Dashboard last updated endpoint
    @GetMapping("/dashboard/last-updated")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getLastUpdated() {
        Map<String, Object> info = Map.of(
            "lastUpdated", System.currentTimeMillis(),
            "message", "Dashboard data được cập nhật mỗi khi có yêu cầu mới"
        );
        return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard last updated info", info));
    }

    // ADMIN ENDPOINTS - Require ADMIN role

    @GetMapping("/admin/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardResponse.DashboardStatsResponse>> getDashboardStats() {
        try {
            authorizationService.checkAdminRole(); // Only ADMIN can access
        } catch (Exception authError) {
            logger.warn("Authorization error in dashboard stats: {}", authError.getMessage());
            return ResponseEntity.status(403).body(new ApiResponse<>(false, "Không có quyền truy cập", null));
        }
        
        try {
            DashboardResponse.DashboardStatsResponse stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy thống kê dashboard thành công", stats));
        } catch (Exception e) {
            logger.error("Dashboard stats error: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy thống kê dashboard: " + e.getMessage(), null));
        }
    }

    @GetMapping("/admin/dashboard/recent-orders")
    public ResponseEntity<ApiResponse<List<DashboardResponse.RecentOrderResponse>>> getRecentOrders() {
        
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            List<DashboardResponse.RecentOrderResponse> recentOrders = dashboardService.getRecentOrders();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách đơn hàng gần đây thành công", recentOrders));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy danh sách đơn hàng gần đây", null));
        }
    }

    @GetMapping("/admin/dashboard/top-products")
    public ResponseEntity<ApiResponse<List<DashboardResponse.TopProductResponse>>> getTopProducts() {
        
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            // Get top products from main stats
            DashboardResponse.DashboardStatsResponse stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách sản phẩm bán chạy thành công", stats.getTopProducts()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy danh sách sản phẩm bán chạy", null));
        }
    }

    @PostMapping("/admin/dashboard/refresh")
    public ResponseEntity<ApiResponse<DashboardResponse.DashboardStatsResponse>> refreshDashboard() {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            DashboardResponse.DashboardStatsResponse stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard được làm mới thành công", stats));
        } catch (Exception e) {
            logger.error("Dashboard refresh error: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi làm mới dashboard: " + e.getMessage(), null));
        }
    }
}
