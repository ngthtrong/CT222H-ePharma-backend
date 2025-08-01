package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.constants.ReportConstants;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.ReportResponse;
import ct222h.vegeta.projectbackend.dto.response.DashboardResponse;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import ct222h.vegeta.projectbackend.service.ReportService;
import ct222h.vegeta.projectbackend.service.AdvancedAnalyticsService;
import ct222h.vegeta.projectbackend.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReportController {

    private final ReportService reportService;
    private final AdvancedAnalyticsService advancedAnalyticsService;
    private final ExcelExportService excelExportService;
    
    @Autowired
    private AuthorizationService authorizationService;

    public ReportController(ReportService reportService, AdvancedAnalyticsService advancedAnalyticsService,
                          ExcelExportService excelExportService) {
        this.reportService = reportService;
        this.advancedAnalyticsService = advancedAnalyticsService;
        this.excelExportService = excelExportService;
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

    // ADVANCED ANALYTICS ENDPOINTS

    @GetMapping("/admin/analytics/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse.AdvancedDashboardMetrics>> getAdvancedDashboardMetrics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        authorizationService.checkAdminRole();
        
        try {
            if (startDate.after(endDate)) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, ReportConstants.ERROR_INVALID_DATE_RANGE, null));
            }
            
            DashboardResponse.AdvancedDashboardMetrics metrics = advancedAnalyticsService.getAdvancedDashboardMetrics(startDate, endDate);
            return ResponseEntity.ok(new ApiResponse<>(true, "Advanced dashboard metrics retrieved successfully", metrics));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to get advanced dashboard metrics", null));
        }
    }

    // EXCEL EXPORT ENDPOINTS

    @GetMapping("/admin/reports/revenue/export/excel")
    public ResponseEntity<byte[]> exportRevenueReportToExcel(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false, defaultValue = "MONTHLY") String reportType) {
        
        authorizationService.checkAdminRole();
        
        try {
            if (startDate.after(endDate)) {
                return ResponseEntity.badRequest().build();
            }
            
            ReportResponse.RevenueReportResponse report = reportService.getRevenueReport(startDate, endDate, reportType);
            
            byte[] excelData = excelExportService.exportRevenueReportToExcel(report);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "revenue-report-" + reportType.toLowerCase() + ".xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/admin/reports/products/export/excel")
    public ResponseEntity<byte[]> exportProductPerformanceToExcel(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        authorizationService.checkAdminRole();
        
        try {
            if (startDate.after(endDate)) {
                return ResponseEntity.badRequest().build();
            }
            
            List<ReportResponse.ProductPerformanceResponse> report = reportService.getProductPerformanceReport(startDate, endDate);
            byte[] excelData = excelExportService.exportProductPerformanceToExcel(report, startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "product-performance-report.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/admin/reports/orders/export/excel")
    public ResponseEntity<byte[]> exportOrderStatisticsToExcel(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        authorizationService.checkAdminRole();
        
        try {
            if (startDate.after(endDate)) {
                return ResponseEntity.badRequest().build();
            }
            
            ReportResponse.OrderStatisticsResponse report = reportService.getOrderStatistics(startDate, endDate);
            byte[] excelData = excelExportService.exportOrderStatisticsToExcel(report);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "order-statistics-report.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/admin/reports/users/export/excel")
    public ResponseEntity<byte[]> exportUserAnalyticsToExcel(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        authorizationService.checkAdminRole();
        
        try {
            if (startDate.after(endDate)) {
                return ResponseEntity.badRequest().build();
            }
            
            ReportResponse.UserAnalyticsResponse report = reportService.getUserAnalytics(startDate, endDate);
            byte[] excelData = excelExportService.exportUserAnalyticsToExcel(report);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "user-analytics-report.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
