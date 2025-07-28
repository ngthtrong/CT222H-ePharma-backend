package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.DashboardResponse;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import ct222h.vegeta.projectbackend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DashboardController {

    private final DashboardService dashboardService;
    
    @Autowired
    private AuthorizationService authorizationService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // ADMIN ENDPOINTS - Require ADMIN role

    @GetMapping("/admin/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardResponse.DashboardStatsResponse>> getDashboardStats() {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            DashboardResponse.DashboardStatsResponse stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy thống kê dashboard thành công", stats));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy thống kê dashboard", null));
        }
    }

    @GetMapping("/admin/dashboard/recent-orders")
    public ResponseEntity<ApiResponse<List<DashboardResponse.RecentOrderResponse>>> getRecentOrders(
            @RequestParam(required = false, defaultValue = "10") int limit) {
        
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            List<DashboardResponse.RecentOrderResponse> recentOrders = dashboardService.getRecentOrders(limit);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách đơn hàng gần đây thành công", recentOrders));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy danh sách đơn hàng gần đây", null));
        }
    }

    @GetMapping("/admin/dashboard/top-products")
    public ResponseEntity<ApiResponse<List<DashboardResponse.TopProductResponse>>> getTopSellingProducts(
            @RequestParam(required = false, defaultValue = "10") int limit) {
        
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            List<DashboardResponse.TopProductResponse> topProducts = dashboardService.getTopSellingProducts(limit);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách sản phẩm bán chạy thành công", topProducts));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy danh sách sản phẩm bán chạy", null));
        }
    }
}
