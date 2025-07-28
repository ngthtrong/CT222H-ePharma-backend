package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.constants.ReportConstants;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.ReportResponse;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import ct222h.vegeta.projectbackend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReportController {

    private final ReportService reportService;
    
    @Autowired
    private AuthorizationService authorizationService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ADMIN ENDPOINTS - Require ADMIN role

    @GetMapping("/admin/reports/revenue")
    public ResponseEntity<ApiResponse<ReportResponse.RevenueReportResponse>> getRevenueReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false, defaultValue = "MONTHLY") String reportType) {
        
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            if (startDate.after(endDate)) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, ReportConstants.ERROR_INVALID_DATE_RANGE, null));
            }
            
            ReportResponse.RevenueReportResponse report = reportService.getRevenueReport(startDate, endDate, reportType);
            return ResponseEntity.ok(new ApiResponse<>(true, ReportConstants.SUCCESS_GET_REVENUE_REPORT, report));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, ReportConstants.ERROR_REPORT_GENERATION_FAILED, null));
        }
    }

    @GetMapping("/admin/reports/products")
    public ResponseEntity<ApiResponse<List<ReportResponse.ProductPerformanceResponse>>> getProductPerformanceReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            if (startDate.after(endDate)) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, ReportConstants.ERROR_INVALID_DATE_RANGE, null));
            }
            
            List<ReportResponse.ProductPerformanceResponse> report = reportService.getProductPerformanceReport(startDate, endDate);
            return ResponseEntity.ok(new ApiResponse<>(true, ReportConstants.SUCCESS_GET_PRODUCT_REPORT, report));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, ReportConstants.ERROR_REPORT_GENERATION_FAILED, null));
        }
    }

    @GetMapping("/admin/reports/orders")
    public ResponseEntity<ApiResponse<ReportResponse.OrderStatisticsResponse>> getOrderStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            if (startDate.after(endDate)) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, ReportConstants.ERROR_INVALID_DATE_RANGE, null));
            }
            
            ReportResponse.OrderStatisticsResponse report = reportService.getOrderStatistics(startDate, endDate);
            return ResponseEntity.ok(new ApiResponse<>(true, ReportConstants.SUCCESS_GET_ORDER_REPORT, report));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, ReportConstants.ERROR_REPORT_GENERATION_FAILED, null));
        }
    }

    @GetMapping("/admin/reports/users")
    public ResponseEntity<ApiResponse<ReportResponse.UserAnalyticsResponse>> getUserAnalytics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            if (startDate.after(endDate)) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, ReportConstants.ERROR_INVALID_DATE_RANGE, null));
            }
            
            ReportResponse.UserAnalyticsResponse report = reportService.getUserAnalytics(startDate, endDate);
            return ResponseEntity.ok(new ApiResponse<>(true, ReportConstants.SUCCESS_GET_USER_REPORT, report));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, ReportConstants.ERROR_REPORT_GENERATION_FAILED, null));
        }
    }
}
