package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.dto.response.ReportResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExcelExportService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public byte[] exportRevenueReportToExcel(ReportResponse.RevenueReportResponse report) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Revenue Report");

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            // Create data style
            CellStyle dataStyle = createDataStyle(workbook);

            int rowNum = 0;

            // Title
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Revenue Report - " + report.getReportType());
            titleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));

            rowNum++; // Empty row

            // Report Information
            createInfoRow(sheet, rowNum++, "Period Start:", report.getPeriodStart() != null ? DATE_FORMAT.format(report.getPeriodStart()) : "N/A", headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Period End:", report.getPeriodEnd() != null ? DATE_FORMAT.format(report.getPeriodEnd()) : "N/A", headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Total Revenue:", String.format("$%.2f", report.getTotalRevenue()), headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Total Orders:", report.getTotalOrders().toString(), headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Generated At:", report.getGeneratedAt() != null ? DATE_FORMAT.format(report.getGeneratedAt()) : "N/A", headerStyle, dataStyle);

            rowNum++; // Empty row

            // Top Products Header
            Row productsHeaderRow = sheet.createRow(rowNum++);
            productsHeaderRow.createCell(0).setCellValue("Top Products");
            productsHeaderRow.getCell(0).setCellStyle(headerStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));

            // Products Table Header
            Row tableHeaderRow = sheet.createRow(rowNum++);
            String[] headers = {"Product ID", "Product Name", "Quantity Sold", "Revenue"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = tableHeaderRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Products Data
            if (report.getTopProducts() != null) {
                for (ReportResponse.ProductPerformanceResponse product : report.getTopProducts()) {
                    Row dataRow = sheet.createRow(rowNum++);
                    dataRow.createCell(0).setCellValue(product.getProductId());
                    dataRow.createCell(1).setCellValue(product.getProductName());
                    dataRow.createCell(2).setCellValue(product.getQuantitySold());
                    dataRow.createCell(3).setCellValue(String.format("$%.2f", product.getRevenue()));

                    for (int i = 0; i < 4; i++) {
                        dataRow.getCell(i).setCellStyle(dataStyle);
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convert to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportProductPerformanceToExcel(List<ReportResponse.ProductPerformanceResponse> products, Date startDate, Date endDate) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Product Performance Report");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            int rowNum = 0;

            // Title
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Product Performance Report");
            titleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 3));

            rowNum++; // Empty row

            // Report Information
            createInfoRow(sheet, rowNum++, "Period Start:", startDate != null ? DATE_FORMAT.format(startDate) : "N/A", headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Period End:", endDate != null ? DATE_FORMAT.format(endDate) : "N/A", headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Generated At:", DATE_FORMAT.format(new Date()), headerStyle, dataStyle);

            rowNum++; // Empty row

            // Table Header
            Row tableHeaderRow = sheet.createRow(rowNum++);
            String[] headers = {"Product ID", "Product Name", "Quantity Sold", "Revenue"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = tableHeaderRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            if (products != null) {
                for (ReportResponse.ProductPerformanceResponse product : products) {
                    Row dataRow = sheet.createRow(rowNum++);
                    dataRow.createCell(0).setCellValue(product.getProductId());
                    dataRow.createCell(1).setCellValue(product.getProductName());
                    dataRow.createCell(2).setCellValue(product.getQuantitySold());
                    dataRow.createCell(3).setCellValue(String.format("$%.2f", product.getRevenue()));

                    for (int i = 0; i < 4; i++) {
                        dataRow.getCell(i).setCellStyle(dataStyle);
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportOrderStatisticsToExcel(ReportResponse.OrderStatisticsResponse stats) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Order Statistics Report");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            int rowNum = 0;

            // Title
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Order Statistics Report");
            titleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 1));

            rowNum++; // Empty row

            // Data
            createInfoRow(sheet, rowNum++, "Period Start:", stats.getPeriodStart() != null ? DATE_FORMAT.format(stats.getPeriodStart()) : "N/A", headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Period End:", stats.getPeriodEnd() != null ? DATE_FORMAT.format(stats.getPeriodEnd()) : "N/A", headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Total Orders:", stats.getTotalOrders().toString(), headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Completed Orders:", stats.getCompletedOrders().toString(), headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Cancelled Orders:", stats.getCancelledOrders().toString(), headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Pending Orders:", stats.getPendingOrders().toString(), headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Average Order Value:", String.format("$%.2f", stats.getAverageOrderValue()), headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Generated At:", stats.getGeneratedAt() != null ? DATE_FORMAT.format(stats.getGeneratedAt()) : "N/A", headerStyle, dataStyle);

            // Auto-size columns
            for (int i = 0; i < 2; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportUserAnalyticsToExcel(ReportResponse.UserAnalyticsResponse analytics) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("User Analytics Report");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            int rowNum = 0;

            // Title
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("User Analytics Report");
            titleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 1));

            rowNum++; // Empty row

            // Data
            createInfoRow(sheet, rowNum++, "Period Start:", analytics.getPeriodStart() != null ? DATE_FORMAT.format(analytics.getPeriodStart()) : "N/A", headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Period End:", analytics.getPeriodEnd() != null ? DATE_FORMAT.format(analytics.getPeriodEnd()) : "N/A", headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Total Users:", analytics.getTotalUsers().toString(), headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "New Users:", analytics.getNewUsers().toString(), headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Active Users:", analytics.getActiveUsers().toString(), headerStyle, dataStyle);
            createInfoRow(sheet, rowNum++, "Generated At:", analytics.getGeneratedAt() != null ? DATE_FORMAT.format(analytics.getGeneratedAt()) : "N/A", headerStyle, dataStyle);

            // Auto-size columns
            for (int i = 0; i < 2; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private void createInfoRow(Sheet sheet, int rowNum, String label, String value, CellStyle headerStyle, CellStyle dataStyle) {
        Row row = sheet.createRow(rowNum);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(headerStyle);
        
        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(dataStyle);
    }
}
